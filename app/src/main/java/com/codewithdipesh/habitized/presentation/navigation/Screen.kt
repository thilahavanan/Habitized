package com.codewithdipesh.habitized.presentation.navigation

import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import java.time.LocalDate

sealed class Screen(val route : String){
    object Home : Screen("home")
    object AddHabit : Screen("addHabit/{date}") {
        fun createRoute(date: LocalDate = LocalDate.now()): String {
            return "addHabit/$date"
        }
    }
    object DurationScreen : Screen("duration/{id}/{title}/{target}/{color}") {
        fun createRoute(habitWithProgress: HabitWithProgress): String {
            val id = habitWithProgress.progress.progressId
            val title = habitWithProgress.habit.title
            val target = habitWithProgress.progress.targetDurationValue
            val color = habitWithProgress.habit.colorKey
            return "duration/$id/$title/$target/$color"
        }
    }
    object AddGoal : Screen("addGoal")
    object Habits : Screen("habits")
}
