package com.codewithdipesh.habitized.presentation.navigation

import com.codewithdipesh.habitized.domain.model.Goal
import com.codewithdipesh.habitized.domain.model.Habit
import com.codewithdipesh.habitized.domain.model.HabitProgress
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

sealed class Screen(val route : String){
    object Home : Screen("home")
    object AddHabit : Screen("addHabit/{id}/{date}") {
        fun createRoute(date: LocalDate = LocalDate.now(),id : String? = ""): String {
            return "addHabit/$id/$date"
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
    object SessionScreen : Screen("session_screen/{id}/{title}/{target}/{color}") {
        fun createRoute(habitWithProgress: HabitWithProgress): String {
            val id = habitWithProgress.progress.progressId
            val title = habitWithProgress.habit.title

            val time = habitWithProgress.progress.targetDurationValue
            val hour = time!!.hour
            val minutes = time.minute
            val seconds = time.second
            val target = (hour*3600) +( minutes*60) + seconds

            val color = habitWithProgress.habit.colorKey
            return "session_screen/$id/$title/$target/$color"
        }
    }
    object AddGoal : Screen("addGoal/{id}"){
        fun createRoute(id : String = ""): String {
            return "addGoal/$id"
        }
    }
    object Progress : Screen("progress")
    object MyThoughts : Screen("myThoughts")
    object AddWidget : Screen("addWidget")
    object HabitScreen : Screen("habit_screen/{id}/{title}/{color}"){
        fun createRoute(habit: Habit): String {
            val id = habit.habit_id
            val title = habit.title
            val color = habit.colorKey
            return "habit_screen/$id/$title/$color"
        }
    }
    object GoalScreen : Screen("goal_screen/{id}/{title}"){
        fun createRoute(goal: Goal?): String {
            val id = goal?.id ?: ""
            val title = goal?.title ?: "Overall Goal"
            return "goal_screen/$id/$title"
        }
    }
}
