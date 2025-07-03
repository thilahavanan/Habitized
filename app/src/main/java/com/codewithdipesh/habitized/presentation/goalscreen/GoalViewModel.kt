package com.codewithdipesh.habitized.presentation.goalscreen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Goal
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.ImageProgress
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import com.codewithdipesh.habitized.presentation.habitscreen.HabitDetailsUI
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID
import kotlin.String

@HiltViewModel
class GoalViewModel @Inject constructor(
    private val repo : HabitRepository
): ViewModel() {

    private val _state = MutableStateFlow(GoalDetailsUI())
    val state = _state.asStateFlow()

    suspend fun init( id : UUID?){
        var goal : Goal?
        var habits = emptyList<Habit>()
        //if id is not null then get the goal from the database
        if(id != null){
            goal = repo.getGoalById(id)
            goal?.let {
                Log.d("goal",goal.start_date.toString())
                _state.value = _state.value.copy(
                    id = id,
                    title = goal.title,
                    description = goal.description ?: "",
                    targetDate = goal.target_date ,
                    startDate = goal.start_date ?: LocalDate.now() ,
                    habits = goal.habits
                )
                habits = goal.habits
            }
        }else{
            //else all habits
            goal = null
            habits = repo.getAllHabits()
            _state.value = _state.value.copy(
                id = null,
                title = "Overall goal",
                description = "",
                habits = habits
            )
        }
        //now others initialization
        var onTrackedHabits = mutableListOf<Habit>()
        var offTrackedHabits = mutableListOf<Habit>()
        var atRiskHabits = mutableListOf<Habit>()
        var closedHabits = mutableListOf<Habit>()
        habits.forEach {
            val completed = repo.getAllCompletedProgress(it.habit_id!!)
            val completionRate = calculateCompletionRate(completed,it,it.frequency)
            when(completionRate) {
                in 0..49 -> {
                    atRiskHabits += it
                }
                in 50..80 -> {
                    offTrackedHabits += it
                }
                in 81..100 -> {
                    onTrackedHabits += it
                }
            }
            if(!it.is_active){
                closedHabits += it
            }
        }
        _state.value = _state.value.copy(
            onTrack = onTrackedHabits,
            offTrack = offTrackedHabits,
            AtRisk = atRiskHabits,
            closed = closedHabits
        )
    }

    //need habit start date , completed progress,frequency
    private fun calculateCompletionRate(completed: List<HabitProgress>,habit:Habit,frequency: Frequency) : Int{
        val total = when(frequency){
            Frequency.Daily -> {
                (LocalDate.now().toEpochDay() - habit.start_date.toEpochDay()).toInt() + 1
            }
            Frequency.Weekly -> {
                val start = habit.start_date
                val now = LocalDate.now()
                val totalWeeks = (start.until(now).days / 7) + 1
                // Count only the scheduled days of the week
                totalWeeks * habit.days_of_week.count { it == 1 }
            }
            Frequency.Monthly -> {
                val start = habit.start_date
                val now = LocalDate.now()
                val months = (now.year - start.year) * 12 + (now.monthValue - start.monthValue) + 1
                months * (habit.daysOfMonth?.size ?: 0)
            }
            else -> 0
        }
        return (completed.size.toFloat() / total.toFloat() * 100).toInt()

    }
}