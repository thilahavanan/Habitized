package com.codewithdipesh.habitized.presentation.timerscreen.sessionScreen

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.habitized.data.services.timerService.TimerService
import com.codewithdipesh.habitized.data.sharedPref.HabitPreference
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.domain.model.Status
import com.codewithdipesh.habitized.domain.model.SubTask
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import com.codewithdipesh.habitized.presentation.timerscreen.Theme
import com.codewithdipesh.habitized.presentation.timerscreen.TimerState
import com.codewithdipesh.habitized.presentation.timerscreen.durationScreen.DurationUI
import com.codewithdipesh.habitized.presentation.util.IntToWeekDayMap
import com.codewithdipesh.habitized.presentation.util.toDays
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import kotlin.collections.get
import kotlin.math.max

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
            },
            isStarted = response.progress.status == Status.Ongoing
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
            putExtra("duration_seconds",totalSeconds)
            putExtra("habit",_state.value.habitWithProgress!!.habit.title)
            putExtra("id",_state.value.progressId.toString())
            putExtra("color",_state.value.habitWithProgress!!.habit.colorKey)
            putExtra("screen","session_screen")
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
        repo.onUpdateCounterHabitProgress(prevCount,_state.value.progressId!!)
        if(prevCount == targetCount){
            _state.value.progressId?.let {
                repo.onDoneHabitProgress(_state.value.progressId!!)
                updateStreak(_state.value.habitWithProgress!!)
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

    fun addSubTask(){
        //add on the ui first then add on the database
        val subTasks = _state.value.habitWithProgress!!.subtasks.toMutableList()
        val newSubtask = SubTask(
            title = "",
            habitProgressId = _state.value.progressId!!
        )
        subTasks.add(newSubtask)
        _state.value = _state.value.copy(
            habitWithProgress = _state.value.habitWithProgress!!.copy(
                subtasks = subTasks
            ),
            tempSubTask = newSubtask.subtaskId
        )
    }
    suspend fun toggleSubTask(index:Int){
        val current = _state.value.habitWithProgress ?: return
        val updatedSubtasks = current.subtasks.toMutableList().apply {
            this[index] = this[index].copy(isCompleted = !this[index].isCompleted)
        }

        val updatedHabit = current.copy(subtasks = updatedSubtasks)

        _state.value = _state.value.copy(habitWithProgress = updatedHabit)

        repo.insertSubtask(updatedSubtasks[index])
    }
    suspend fun updateSubTask(index: Int, title: String){
        //if new task is being written
        if(_state.value.habitWithProgress!!.subtasks[index].subtaskId
            == _state.value.tempSubTask && title != "")
        {
            _state.value = _state.value.copy(
                tempSubTask = null
            )
        }
        val current = _state.value.habitWithProgress ?: return
        val updatedSubtasks = current.subtasks.toMutableList().apply {
            this[index] = this[index].copy(title = title)
        }

        val updatedHabit = current.copy(subtasks = updatedSubtasks)

        _state.value = _state.value.copy(habitWithProgress = updatedHabit)

        repo.insertSubtask(updatedSubtasks[index])
    }
    suspend fun deleteSubTask(id: UUID){
        val current = _state.value.habitWithProgress ?: return
        val updatedSubtasks = current.subtasks.toMutableList().apply {
             removeIf{it.subtaskId == id}
        }

        val updatedHabit = current.copy(subtasks = updatedSubtasks)

        _state.value = _state.value.copy(habitWithProgress = updatedHabit)

        repo.deleteSubtask(id)
    }

    suspend fun updateStreak(habitWithProgress: HabitWithProgress,isSkipped : Boolean = false){
        val completedDates = repo.getAllCompletedDates(habitWithProgress.habit.habit_id!!)
        val streak = calculateCurrentStreak(habitWithProgress.habit,completedDates)
        repo.updateStreak(
            habitId = habitWithProgress.habit.habit_id,
            current = streak,
            max = max(streak,habitWithProgress.habit.maxStreak)
        )

    }
    fun calculateCurrentStreak(
        habit: Habit,
        completedDatesDesc: List<LocalDate>
    ): Int {
        var streak = 0
        var expectedDate = LocalDate.now()

        for (date in completedDatesDesc) {
            // Skip non-scheduled days
            expectedDate = getPreviousScheduledDate(habit,expectedDate)

            if (date == expectedDate) {
                streak++
                expectedDate = expectedDate.minusDays(1)
            } else {
                break
            }
        }
        return streak
    }
    fun getPreviousScheduledDate(habit: Habit, fromDate: LocalDate): LocalDate {
        var date = fromDate
        while (true) {
            when (habit.frequency) {
                Frequency.Weekly -> {
                    val frequencyMap = IntToWeekDayMap(habit.days_of_week)
                    if (frequencyMap[date.dayOfWeek.toDays()] == true) return date
                }
                Frequency.Monthly -> {
                    val frequency = habit.daysOfMonth ?: emptyList()
                    if (frequency.contains(date.dayOfMonth)) return date
                }
                else -> return date
            }
            date = date.minusDays(1)
        }
    }



}