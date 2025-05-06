package com.codewithdipesh.habitized.presentation.navigation

sealed class Screen(val route : String){
    object Home : Screen("home")
}
