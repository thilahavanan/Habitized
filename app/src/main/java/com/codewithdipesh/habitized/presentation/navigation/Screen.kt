package com.codewithdipesh.habitized.presentation.navigation

import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import java.time.LocalDate
import java.time.LocalTime

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

            val time = habitWithProgress.progress.targetDurationValue
            val hour = time!!.hour
            val minutes = time.minute
            val seconds = time.second
            val target = (hour*3600) +( minutes*60) + seconds

            val color = habitWithProgress.habit.colorKey
            return "duration/$id/$title/$target/$color"
        }
        fun createRoute(id: String, title: String, target: Int, color: String) =
            "duration/$id/$title/$target/$color"
    }
    object AddGoal : Screen("addGoal")
    object Habits : Screen("habits")
}
