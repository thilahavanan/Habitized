package com.codewithdipesh.habitized.presentation.goalscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codewithdipesh.habitized.domain.model.Frequency
import com.codewithdipesh.habitized.domain.model.Goal
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.domain.model.ImageProgress
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import com.codewithdipesh.habitized.presentation.goalscreen.components.GraphType
import com.codewithdipesh.habitized.presentation.habitscreen.HabitDetailsUI
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.temporal.ChronoUnit
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
        }
        else{
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
        //set efforts
        //so for calculating 365 days it takes time so theres a slight delay in UI
        //calling 7 days first os it shows on UI instantly and calculate rest 364 days in background
        viewModelScope.launch(Dispatchers.IO){
            setEfforts(goal)
        }
        viewModelScope.launch(Dispatchers.IO){
            setEfforts(goal,364)
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

    //get progress efforts
    private fun setEfforts(goal: Goal?,days : Int = 6) {
        viewModelScope.launch(Dispatchers.IO) {
            val start_date = LocalDate.now().minusDays(days.toLong())
            val target_date = LocalDate.now() 

            val effortList = mutableListOf<Effort>()

            for (i in 0..(target_date.toEpochDay() - start_date!!.toEpochDay())) {
                val day = start_date.plusDays(i)
                val habits = goal?.let {
                    repo.getHabitsByGoalForDate(it.id, day)
                } ?: repo.getAllHabitsForDate(day)
                if(habits.isNotEmpty()){
                    val progressList = habits.flatMap { habit ->
                        listOf(repo.checkHabitDoneOrNot(habit.habit_id!!, day))
                    }

                    val average = if (progressList.isNotEmpty()) {
                        progressList.count { it } / progressList.size.toFloat()
                    } else 0f

                    effortList += Effort(day, average * 100) // <-- Use Effort(day, effortLevel)
                }
            }

            _state.value = _state.value.copy(effortList = effortList)
            setShowedEfforts(_state.value.showedGraphType)
        }
    }

    //update showed progres
    fun setShowedEfforts(type : GraphType){
        when(type){
            GraphType.last_week -> {
                val last7Days = (0..6).map { LocalDate.now().minusDays(it.toLong()) }.toSet()
                _state.value = _state.value.copy(
                    showedGraphType = type,
                    showedEfforts = _state.value.effortList.filter {it.day in last7Days}
                )
            }
            GraphType.last_month -> {
                val last30Days = (0..29).map { LocalDate.now().minusDays(it.toLong()) }.toSet()
                _state.value = _state.value.copy(
                    showedGraphType = type,
                    showedEfforts = _state.value.effortList.filter {it.day in last30Days}
                )
            }
            GraphType.last_year -> {
                val last365Days = (0..364).map { LocalDate.now().minusDays(it.toLong()) }.toSet()
                _state.value = _state.value.copy(
                    showedGraphType = type,
                    showedEfforts = _state.value.effortList.filter {it.day in last365Days}
                )
            }
        }
    }

    fun clearUi(){
        _state.value = GoalDetailsUI()
    }

}