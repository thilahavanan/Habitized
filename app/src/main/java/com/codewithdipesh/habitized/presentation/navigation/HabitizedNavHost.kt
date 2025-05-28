package com.codewithdipesh.habitized.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.codewithdipesh.habitized.presentation.addscreen.AddViewModel
import com.codewithdipesh.habitized.presentation.addscreen.addGoalScreen.AddGoalScreen
import com.codewithdipesh.habitized.presentation.addscreen.addhabitscreen.AddHabitScreen
import com.codewithdipesh.habitized.presentation.homescreen.HomeScreen
import com.codewithdipesh.habitized.presentation.homescreen.HomeViewModel

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HabitizedNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    addViewModel: AddViewModel
){
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                navController = navController,
                viewmodel = homeViewModel
            )
        }
        composable(Screen.AddHabit.route) {
            AddHabitScreen(
                navController = navController,
                viewmodel = addViewModel
            )
        }
        composable(Screen.AddGoal.route){
            AddGoalScreen(
                navController = navController,
                viewmodel = addViewModel
            )
        }
    }
}