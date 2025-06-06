package com.codewithdipesh.habitized.presentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.codewithdipesh.habitized.presentation.addscreen.AddViewModel
import com.codewithdipesh.habitized.presentation.addscreen.addGoalScreen.AddGoalScreen
import com.codewithdipesh.habitized.presentation.addscreen.addhabitscreen.AddHabitScreen
import com.codewithdipesh.habitized.presentation.homescreen.HomeScreen
import com.codewithdipesh.habitized.presentation.homescreen.HomeViewModel
import com.codewithdipesh.habitized.presentation.timerscreen.durationScreen.DurationScreen
import com.codewithdipesh.habitized.presentation.timerscreen.durationScreen.DurationViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun HabitizedNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    addViewModel: AddViewModel,
    durationViewModel : DurationViewModel
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
        composable(
            Screen.AddHabit.route,
            arguments = listOf(
                navArgument("date"){
                    type = NavType.StringType
                    defaultValue = LocalDate.now().toString()
                }
            )
        ){  entry ->
            val dateStr = entry.arguments?.getString("date")
            val date = LocalDate.parse(dateStr)
            AddHabitScreen(
                navController = navController,
                viewmodel = addViewModel,
                date= date
            )
        }
        composable(
            Screen.DurationScreen.route,
            arguments = listOf(
                navArgument("id"){
                    type = NavType.StringType
                },
                navArgument("title"){
                    type = NavType.StringType
                },
                navArgument("target"){
                    type = NavType.StringType
                },
                navArgument("color"){
                    type = NavType.StringType
                }
            )
        ){ entry ->
            val id = entry.arguments?.getString("id")
            val title = entry.arguments?.getString("title")
            val color = entry.arguments?.getString("color")
            val target = entry.arguments?.getString("target")
            val targetDurationValue = LocalTime.parse(target)
            DurationScreen(
                habitProgressId = UUID.fromString(id),
                title = title!!,
                targetDurationValue = targetDurationValue,
                colorKey = color!!,
                navController = navController,
                viewmodel = durationViewModel
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