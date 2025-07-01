package com.codewithdipesh.habitized.presentation.navigation

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.codewithdipesh.habitized.presentation.addscreen.AddViewModel
import com.codewithdipesh.habitized.presentation.addscreen.addGoalScreen.AddGoalScreen
import com.codewithdipesh.habitized.presentation.addscreen.addhabitscreen.AddHabitScreen
import com.codewithdipesh.habitized.presentation.habitscreen.HabitDetails
import com.codewithdipesh.habitized.presentation.habitscreen.HabitViewModel
import com.codewithdipesh.habitized.presentation.homescreen.HomeScreen
import com.codewithdipesh.habitized.presentation.homescreen.HomeViewModel
import com.codewithdipesh.habitized.presentation.progress.ProgressScreen
import com.codewithdipesh.habitized.presentation.progress.ProgressViewModel
import com.codewithdipesh.habitized.presentation.timerscreen.durationScreen.DurationViewModel
import com.codewithdipesh.habitized.presentation.timerscreen.durationScreen.DurationScreen
import com.codewithdipesh.habitized.presentation.timerscreen.sessionScreen.SessionScreen
import com.codewithdipesh.habitized.presentation.timerscreen.sessionScreen.SessionViewModel
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
    durationViewModel : DurationViewModel,
    sessionViewModel : SessionViewModel,
    progressViewModel : ProgressViewModel,
    habitViewModel : HabitViewModel
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
                navArgument("id"){
                    type = NavType.StringType
                    defaultValue = ""
                },
                navArgument("date"){
                    type = NavType.StringType
                    defaultValue = LocalDate.now().toString()
                }
            )
        ){  entry ->
            val dateStr = entry.arguments?.getString("date")
            val date = LocalDate.parse(dateStr)
            val id = entry.arguments?.getString("id") ?: null
            AddHabitScreen(
                navController = navController,
                viewmodel = addViewModel,
                date= date,
                id = if(id.isNullOrEmpty()) null else UUID.fromString(id)
            )
        }
        composable(
            Screen.DurationScreen.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "com.codewithdipesh.habitized://duration/{id}/{title}/{target}/{color}"
                    action = Intent.ACTION_VIEW
                }
            ),
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
            val targetSeconds = entry.arguments?.getString("target")!!.toInt()

            val hour = targetSeconds/ 3600
            val minutes = (targetSeconds % 3600) / 60
            val seconds = targetSeconds % 60
            val targetDurationValue = LocalTime.of(hour,minutes,seconds)

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
        composable(
            Screen.SessionScreen.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "com.codewithdipesh.habitized://session_screen/{id}/{title}/{target}/{color}"
                    action = Intent.ACTION_VIEW
                }
            ),
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
            val targetSeconds = entry.arguments?.getString("target")!!.toInt()

            val hour = targetSeconds/ 3600
            val minutes = (targetSeconds % 3600) / 60
            val seconds = targetSeconds % 60
            val targetDurationValue = LocalTime.of(hour,minutes,seconds)

            SessionScreen(
                habitProgressId = UUID.fromString(id),
                title = title!!,
                targetDurationValue = targetDurationValue,
                colorKey = color!!,
                navController = navController,
                viewmodel = sessionViewModel
            )
        }
        composable(Screen.Progress.route) {
            ProgressScreen(
                navController = navController,
                viewmodel = progressViewModel
            )
        }
        composable(
            Screen.HabitScreen.route,
            arguments = listOf(
                navArgument("id"){
                    type = NavType.StringType
                },
                navArgument("title"){
                    type = NavType.StringType
                },
                navArgument("color"){
                    type = NavType.StringType
                }
            )
        ) {entry->
            val id = entry.arguments?.getString("id")
            val title = entry.arguments?.getString("title")
            val color = entry.arguments?.getString("color")

            HabitDetails(
                id = UUID.fromString(id),
                title = title!! ,
                colorKey = color!!,
                navController = navController,
                viewmodel = habitViewModel
            )
        }
    }
}