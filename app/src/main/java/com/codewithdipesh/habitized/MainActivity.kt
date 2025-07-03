 package com.codewithdipesh.habitized

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.codewithdipesh.habitized.domain.model.Goal
import com.codewithdipesh.habitized.presentation.addscreen.AddViewModel
import com.codewithdipesh.habitized.presentation.goalscreen.GoalViewModel
import com.codewithdipesh.habitized.presentation.habitscreen.HabitViewModel
import com.codewithdipesh.habitized.presentation.homescreen.HomeViewModel
import com.codewithdipesh.habitized.presentation.navigation.HabitizedNavHost
import com.codewithdipesh.habitized.presentation.progress.ProgressViewModel
import com.codewithdipesh.habitized.presentation.timerscreen.durationScreen.DurationViewModel
import com.codewithdipesh.habitized.presentation.timerscreen.sessionScreen.SessionViewModel
import com.codewithdipesh.habitized.ui.theme.HabitizedTheme
import dagger.hilt.android.AndroidEntryPoint

 @AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.POST_NOTIFICATIONS,
                    Manifest.permission.FOREGROUND_SERVICE,
                    Manifest.permission.FOREGROUND_SERVICE_DATA_SYNC,
                    Manifest.permission.FOREGROUND_SERVICE_MEDIA_PLAYBACK
                ),
                0
            )
        }
        setContent {
            HabitizedTheme(){
                val navController = rememberNavController()
                val homeViewModel by viewModels<HomeViewModel>()
                val addViewModel by viewModels<AddViewModel>()
                val durationViewModel by viewModels<DurationViewModel>()
                val sessionViewModel by viewModels<SessionViewModel>()
                val progressViewModel by viewModels<ProgressViewModel>()
                val habitViewModel by viewModels<HabitViewModel>()
                val goalViewModel by viewModels<GoalViewModel>()
                HabitizedNavHost(
                    navController = navController,
                    homeViewModel = homeViewModel,
                    addViewModel = addViewModel,
                    durationViewModel = durationViewModel,
                    sessionViewModel = sessionViewModel,
                    progressViewModel = progressViewModel,
                    habitViewModel = habitViewModel,
                    goalViewModel = goalViewModel
                )
            }
        }
    }
}

