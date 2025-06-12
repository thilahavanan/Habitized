package com.codewithdipesh.habitized.data.services

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.codewithdipesh.habitized.presentation.timerscreen.durationScreen.TimerState
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


    private val _timerState = MutableStateFlow(TimerClassState(0, 0, 0, false))
    val timerState: StateFlow<TimerClassState> = _timerState.asStateFlow()

    private val connection = object : ServiceConnection {
        override fun onServiceConnected(className: ComponentName, service: IBinder) {
            val binder = service as TimerService.TimerBind
            timerService = binder.getService()
            isBound = true

            timerService?.setTimerCallback(object : TimerService.TimerCallback {
                override fun onTimerUpdate(h: Int, m: Int, s: Int) {
                    _timerState.value = TimerClassState(h, m, s, false)
                    callbacks.forEach { it.onTimerUpdate(h, m, s) }
                }

                override fun onTimerFinished() {
                    _timerState.value = TimerClassState(0, 0, 0, true)
                    callbacks.forEach { it.onTimerFinished() }
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(1000)
                        _timerState.value = _timerState.value.copy(isFinished = false)
                    }
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
}


