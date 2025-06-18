package com.codewithdipesh.habitized.presentation.timerscreen.sessionScreen

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.habitized.data.services.timerService.TimerService
import com.codewithdipesh.habitized.data.sharedPref.HabitPreference
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.domain.model.Status
import com.codewithdipesh.habitized.domain.model.SubTask
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import com.codewithdipesh.habitized.presentation.timerscreen.Theme
import com.codewithdipesh.habitized.presentation.timerscreen.TimerState
import com.codewithdipesh.habitized.presentation.timerscreen.durationScreen.DurationUI
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.collections.get

@HiltViewModel
class SessionViewModel @Inject constructor(
    private val repo : HabitRepository,
    private val pref : HabitPreference
) : ViewModel(){

     private val _state = MutableStateFlow(SessionUI())
     val state = _state.asStateFlow()

    init {
        val theme = pref.getTheme(_state.value.theme.displayName)
        _state.value = _state.value.copy(
            theme = Theme.Companion.fromString(theme)
        )
    }

    suspend fun init(id : UUID){
        val response = repo.getHabitProgressById(id)
        _state.value = _state.value.copy(
            progressId = id,
            habitWithProgress = response,
            timerState = when(response.progress.status){
                is Status.Done -> TimerState.Finished
                Status.Ongoing -> TimerState.Resumed
                else -> TimerState.Not_Started
            }
        )
    }

    fun resumeTimer(){
        _state.value = _state.value.copy(
            timerState = TimerState.Resumed
        )
    }
    fun pauseTimer(){
        _state.value = _state.value.copy(
            timerState = TimerState.Paused
        )
    }
    fun finishTimer(){
        _state.value = _state.value.copy(
            timerState = TimerState.Finished
        )
    }
    suspend fun cancelTimer(){
        repo.onNotStartedHabitProgress(_state.value.progressId!!)
        _state.value = _state.value.copy(
            timerState = TimerState.Not_Started,
            isStarted = false
        )
    }

    fun clearUi(){
        _state.value = _state.value.copy(
            progressId = null,
            habitWithProgress = null,
            timerState = TimerState.Not_Started,
            isThemesOpen=false
        )
    }

    suspend fun startTimer(context : Context, totalSeconds : Int){
        _state.value = _state.value.copy(
            isStarted = true,
            timerState = TimerState.Resumed
        )
        val intent = Intent(context, TimerService::class.java).apply {
            Log.d("timerservicw-viewmodel","$totalSeconds")
            putExtra("duration_seconds",totalSeconds)
            putExtra("habit",_state.value.habitWithProgress!!.habit.title)
            putExtra("id",_state.value.progressId.toString())
            putExtra("color",_state.value.habitWithProgress!!.habit.colorKey)
            putExtra("screen","session")
        }
        context.startForegroundService(intent)
        repo.onStartedHabitProgress(_state.value.progressId!!)
    }
    suspend fun finishHabit(){
        //count++
        //ongoing->not started
        //if(count reached target then ongoing-> finished
        var prevCount = _state.value.habitWithProgress!!.progress.currentCount
        val targetCount = _state.value.habitWithProgress!!.progress.targetCount
        prevCount = prevCount!! + 1
        if(prevCount == targetCount){
            _state.value.progressId?.let {
                repo.onDoneHabitProgress(_state.value.progressId!!)
            }
        }else{
            _state.value.progressId?.let {
                repo.onNotStartedHabitProgress(_state.value.progressId!!)
            }
        }

    }

    fun chooseTheme(theme: Theme){
        _state.value = _state.value.copy(
            theme = theme
        )
        pref.updateTheme(theme.displayName)
    }
    fun openThemes(){
        _state.value = _state.value.copy(
            isThemesOpen = true
        )
    }
    fun closeThemes(){
        _state.value = _state.value.copy(
            isThemesOpen = false
        )
    }

    suspend fun addUpdateSubTasks(subtasks : List<SubTask>){
        //add on the ui first then add on the database

        //change in local first(Ui)
        var updatedhabit = _state.value.habitWithProgress!!
        updatedhabit = updatedhabit.also {
            it.copy(subtasks = subtasks)
        }
        _state.value = _state.value.copy(
            habitWithProgress = updatedhabit
        )
        //change in room
        val previous = repo.getSubtasks(habitProgerssId = _state.value.progressId!!)

        val deleted = previous.filter { !subtasks.contains(it) }
        val added = subtasks.filter { !previous.contains(it) }
        val updated = subtasks.filter { previous.contains(it) }

        viewModelScope.launch(Dispatchers.IO) {
            added.forEach {
                repo.insertSubtask(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            updated.forEach {
                repo.insertSubtask(it)
            }
        }
        viewModelScope.launch(Dispatchers.IO) {
            deleted.forEach {
                repo.deleteSubtask(it.subtaskId)
            }
        }
    }
    suspend fun toggleSubTask(index:Int){
        val previous = _state.value.habitWithProgress!!.subtasks[index].isCompleted
        _state.value = _state.value.copy(
            habitWithProgress = _state.value.habitWithProgress!!
                .also {
                    it.subtasks
                        .toMutableList()
                        .also {
                            it[index] = it[index].copy(isCompleted = !previous)
                        }
                }
        )
        repo.insertSubtask(_state.value.habitWithProgress!!.subtasks[index])
    }




}