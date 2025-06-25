package com.codewithdipesh.habitized.presentation.progress

import com.codewithdipesh.habitized.domain.model.Goal
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import java.time.LocalDate

data class ProgressUI(
    val date : LocalDate = LocalDate.now(),
    val selectedOption : Options = Options.Weekly,
    val WeeklyDateRange : List<LocalDate> = emptyList(),
    val OverAllDateRange : List<LocalDate> = emptyList(),
    val selectedGoal : Goal? = null,
    val habits : List<HabitWithWeeklyAndOverallProgress> = emptyList()
)


data class HabitWithWeeklyAndOverallProgress(
    val habit : Habit,
    val WeeklyProgresses : List<HabitProgress>,
    val OverallProgresses : List<HabitProgress>
)

enum class Options{
    Weekly,
    Overall
}