package com.codewithdipesh.habitized.presentation.homescreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.domain.model.Status
import com.codewithdipesh.habitized.domain.model.SubTask
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import com.codewithdipesh.habitized.presentation.util.IntToWeekDayMap
import com.codewithdipesh.habitized.presentation.util.toDays
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo : HabitRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(HomeScreenUI())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = _uiState.value.copy(
            selectedDate = LocalDate.now()
        )
        loadHomePage(_uiState.value.selectedDate)
    }

    fun loadHomePage(date: LocalDate){
        viewModelScope.launch(Dispatchers.IO){
            val habits = repo.getHabitsForDay(date)
            val tasks = repo.getTasksForDay(date)
            //checking for ongoing duration or session habit
            val ongoingHabit = habits
                .asSequence()
                .filter { habit ->
                    (habit.habit.type == HabitType.Duration || habit.habit.type == HabitType.Session) &&
                            habit.progress.status == Status.Ongoing
                }
                .firstOrNull()
            Log.d("Ongoing",ongoingHabit.toString())
            // Update ongoing timer if habit exists
            ongoingHabit?.let { addOngoingTimer(it) }
            _uiState.value = _uiState.value.copy(
                habitWithProgressList = habits,
                tasks = tasks,
                ongoingHabit = ongoingHabit
            )
        }
    }

    fun onOptionSelected(option: HomeScreenOption){
        _uiState.value = _uiState.value.copy(
            selectedOption = option
        )
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.value = _uiState.value.copy(
            selectedDate = date
        )
        loadHomePage(date)
    }

    fun toggleDatePicker(){
        _uiState.value = _uiState.value.copy(
            isShowingDatePicker = !_uiState.value.isShowingDatePicker
        )
    }

    suspend fun addUpdateSubTasks(subtasks : List<SubTask>, habitProgressId : UUID){
        //add on the ui first then add on the database

        //change in local first(Ui)
        var updatedList = _uiState.value.habitWithProgressList.toMutableList()
        updatedList = updatedList.map {
            if(it.progress.progressId == habitProgressId){
                it.copy(subtasks = subtasks)
            }else{
                it
            }
        } as MutableList<HabitWithProgress>
        _uiState.value = _uiState.value.copy(
            habitWithProgressList = updatedList
        )
        //change in room
        val previous = repo.getSubtasks(habitProgerssId = habitProgressId )

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

    fun toggleSubtask(subtask: SubTask){
        val updatedList = _uiState.value.habitWithProgressList.map {habitWithProgress->
            if(habitWithProgress.habit.type == HabitType.Session && habitWithProgress.subtasks.contains(subtask)){
                val updatedSubTasks = habitWithProgress.subtasks.map{
                    if(it.subtaskId == subtask.subtaskId) it.copy(isCompleted = !it.isCompleted) else it
                }
                habitWithProgress.copy(subtasks = updatedSubTasks)
            } else habitWithProgress
        }

        _uiState.value = _uiState.value.copy(
            habitWithProgressList = updatedList
        )
        viewModelScope.launch(Dispatchers.IO) {
            repo.toggleSubTask(subtask.subtaskId)
        }
    }

    fun onSkipHabit(habitProgress : HabitProgress){
        viewModelScope.launch(Dispatchers.IO){
            repo.onSkipHabitProgress(progressId = habitProgress.progressId)
        }
    }
    fun onDoneHabit(habitWithProgress: HabitWithProgress){
        viewModelScope.launch(Dispatchers.IO){
            repo.onDoneHabitProgress(progressId = habitWithProgress.progress.progressId)
            updateStreak(habitWithProgress)
        }
    }
    fun onUnSkipDoneHabit(habitWithProgress: HabitWithProgress){
        viewModelScope.launch(Dispatchers.IO){
            repo.onNotStartedHabitProgress(progressId = habitWithProgress.progress.progressId)
            updateStreak(habitWithProgress,true)
        }
    }

    fun onUpdateCounter(count : Int, habitWithProgress : HabitWithProgress){
        //local ui
        val ifDone = count >= (habitWithProgress.progress.targetCount ?: 0)
        val prevStatus = habitWithProgress.progress.status
        val updatedList = _uiState.value.habitWithProgressList.map {it->
            if(it.progress.progressId == habitWithProgress.progress.progressId){
                it.copy(
                    progress = habitWithProgress.progress.copy(
                        currentCount = count,
                        status = if(ifDone) Status.Done
                                 else Status.NotStarted
                    )
                )
            }else{
                it
            }
        }

        _uiState.value = _uiState.value.copy(
            habitWithProgressList = updatedList
        )
        viewModelScope.launch(Dispatchers.IO){
            repo.onUpdateCounterHabitProgress(count,habitWithProgress.progress.progressId)
        }
        if(ifDone) {
            onDoneHabit(habitWithProgress)
        }
        else{//update only if prev was done -> then change to not started
            if(prevStatus != Status.NotStarted){
                onUnSkipDoneHabit(habitWithProgress)
            }
        }

    }

    fun addOngoingTimer(habitWithProgress: HabitWithProgress){
        if(_uiState.value.ongoingHabit == null){
            _uiState.value = _uiState.value.copy(
                ongoingHabit = habitWithProgress
            )
        }
    }
    fun updateOngoingTimer(hour: Int, minute: Int, second: Int){
        _uiState.value = _uiState.value.copy(
            ongoingHour = hour,
            ongoingMinute = minute,
            ongoingSecond = second
        )
    }
    suspend fun finishTimer(){
        if(_uiState.value.ongoingHabit != null && _uiState.value.ongoingHabit!!.habit.type == HabitType.Duration){
            _uiState.value.ongoingHabit?.let {
                repo.onDoneHabitProgress(_uiState.value.ongoingHabit!!.progress.progressId)
                updateStreak(_uiState.value.ongoingHabit!!)
            }
        }else if(_uiState.value.ongoingHabit != null){
            //session
            var prevCount = _uiState.value.ongoingHabit!!.progress.currentCount
            val targetCount = _uiState.value.ongoingHabit!!.progress.targetCount
            prevCount = prevCount!! + 1
            if(prevCount == targetCount){
                repo.onDoneHabitProgress(_uiState.value.ongoingHabit!!.progress.progressId)
                updateStreak(_uiState.value.ongoingHabit!!)
            }else{
                repo.onNotStartedHabitProgress(_uiState.value.ongoingHabit!!.progress.progressId)
            }
        }
    }

    suspend fun updateStreak(habitWithProgress: HabitWithProgress,isSkipped : Boolean = false){
        val completedDates = repo.getAllCompletedDates(habitWithProgress.habit.habit_id!!)
        val streak = calculateCurrentStreak(habitWithProgress.habit,completedDates)
        Log.d("streak",streak.toString())
        repo.updateStreak(
            habitId = habitWithProgress.habit.habit_id,
            current = streak,
            max = if(isSkipped) {
                if(habitWithProgress.habit.maxStreak == habitWithProgress.habit.currentStreak) max(streak-1,0)
                else max(streak,habitWithProgress.habit.maxStreak)
            }
            else max(streak,habitWithProgress.habit.maxStreak)
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