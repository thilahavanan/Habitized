package com.codewithdipesh.habitized.data.services.timerService

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TimerServiceManager(
    private val context : Context
){
    private var timerService: TimerService? = null
    private var isBound = false
    private var serviceConnection: ServiceConnection? = null
    private val callbacks = mutableSetOf<TimerService.TimerCallback>()


    private val _timerState = MutableStateFlow(TimerClassState(0, 0, 0, false,false))
    val timerState: StateFlow<TimerClassState> = _timerState.asStateFlow()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.TimerBind
            timerService = binder.getService()
            isBound = true

            // 🔥 Immediately sync current state
            timerService?.timerState?.value?.let { currentState ->
                _timerState.value = currentState
            }

            timerService?.setTimerCallback(object : TimerService.TimerCallback {
                override fun onTimerUpdate(h: Int, m: Int, s: Int) {
                    _timerState.value = _timerState.value.copy(
                        hour = h,
                        minute = m,
                        second = s
                    )
                    callbacks.forEach { it.onTimerUpdate(h, m, s) }
                }

                override fun onTimerFinished() {
                    _timerState.value = _timerState.value.copy(
                        isPaused = false,
                       isFinished = true
                    )
                    callbacks.forEach { it.onTimerFinished() }
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        _timerState.value = _timerState.value.copy(isFinished = false)
                    }
                }

                override fun onTimerPaused() {
                    _timerState.value = _timerState.value.copy(
                        isPaused = true
                    )
                    callbacks.forEach { it.onTimerPaused() }
                }

                override fun onTimerStarted() {
                    _timerState.value = _timerState.value.copy(
                        isPaused = false
                    )
                    callbacks.forEach { it.onTimerFinished() }
                }
            })
        }

        override fun onServiceDisconnected(arg0: ComponentName) {
            timerService = null
            isBound = false
        }
    }

    fun bindService() {
        if (!isBound && serviceConnection == null) {
            val intent = Intent(context, TimerService::class.java)
            context.bindService(intent, connection, Context.BIND_AUTO_CREATE)
            serviceConnection = connection
        }
    }

    fun unbindService() {
        if (isBound && serviceConnection != null) {
            context.unbindService(serviceConnection!!)
            serviceConnection = null
            isBound = false
            timerService = null
        }
    }
    fun pause() {
        timerService?.pauseTimer()
    }
    fun complete(){
        timerService?.finishTimer()
    }
    fun resume() {
        timerService?.resumeTimer()
    }
    fun cancel(){
        timerService?.cancel()
    }
}