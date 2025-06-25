package com.codewithdipesh.habitized.presentation.progress

import android.util.Log
import androidx.core.util.toRange
import androidx.lifecycle.ViewModel
import com.codewithdipesh.habitized.domain.model.Goal
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

@HiltViewModel
class ProgressViewModel @Inject constructor(
    private val repo : HabitRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProgressUI())
    val state = _state.asStateFlow()

    init {
        getWeekDateRange()
        getMonthDateRange()
    }
    suspend fun getHabitProgresses(){
        //all habits
        var finalHabits = emptyList<HabitWithWeeklyAndOverallProgress>()
        val habits = repo.getAllHabits()
        Log.d("progress",habits.toString())
        habits.map {
            //all progress
            val progresses = repo.getAllHabitProgress(it.habit_id!!)
            var overallProgresses = emptyList<HabitProgress>()
            var weeklyProgresses = emptyList<HabitProgress>()
            _state.value.OverAllDateRange.forEach {
                //if date matches with monthly range
                val overallPresent = progresses
                    ?.find{ it.date == _state.value.date}
                if(overallPresent != null){
                    overallProgresses = overallProgresses.plus(overallPresent)
                    //if overall present means weekly also
                    if(_state.value.WeeklyDateRange.contains(overallPresent.date)){
                        weeklyProgresses = weeklyProgresses.plus(overallPresent)
                    }
                }
            }
            finalHabits += HabitWithWeeklyAndOverallProgress(
                habit = it,
                WeeklyProgresses = weeklyProgresses,
                OverallProgresses = overallProgresses
            )
        }
        _state.value = _state.value.copy(
            habits = finalHabits,
            showedHabits = finalHabits
        )


    }

    private fun getWeekDateRange(){
        val startOfWeek = _state.value.date.with(DayOfWeek.MONDAY)
        _state.value = _state.value.copy(
            WeeklyDateRange = (0..6).map { offset ->
                startOfWeek.plusDays(offset.toLong())
            }
        )
    }

    private fun getMonthDateRange(){
        _state.value = _state.value.copy(
            OverAllDateRange =
                //the last column is subjective monday to sunday
                //so range can be 141-147 ( 140 is fixed then calculate -->1--7
                (0..(133 + _state.value.date.dayOfWeek.value) )
                    .map{ offset ->
                        _state.value.date.minusDays(offset.toLong())
                    }
                    .reversed()

        )

    }

    suspend fun getAllGoals(){
        val goals = repo.getAllGoals()
        _state.value = _state.value.copy(
            goals = goals
        )
    }

    fun selectGoal(goal : Goal?){
        if(goal == null){
            _state.value = _state.value.copy(
                selectedGoal = null,
                showedHabits = _state.value.habits
            )
        }else{
            val goal = _state.value.goals.find { it.id == goal.id }
            _state.value = _state.value.copy(
                selectedGoal = goal,
                showedHabits = _state.value.habits.filter { it.habit.goal_id == goal!!.id }
            )
        }
    }

    fun setOption(option : Options){
        _state.value = _state.value.copy(
            selectedOption = option
        )

    }



}