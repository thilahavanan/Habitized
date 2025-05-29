package com.codewithdipesh.habitized.presentation.navigation

import java.time.LocalDate

sealed class Screen(val route : String){
    object Home : Screen("home")
    object AddHabit : Screen("addHabit/{date}") {
        fun createRoute(date: LocalDate = LocalDate.now()): String {
            return "addHabit/$date"
        }
    }
    object AddGoal : Screen("addGoal")
    object Habits : Screen("habits")
}
