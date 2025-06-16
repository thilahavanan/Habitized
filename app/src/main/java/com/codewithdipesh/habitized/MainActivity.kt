 package com.codewithdipesh.habitized

import android.Manifest
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.ServiceConnection
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.navigation.compose.rememberNavController
import com.codewithdipesh.habitized.data.services.timerService.TimerService
import com.codewithdipesh.habitized.presentation.addscreen.AddViewModel
import com.codewithdipesh.habitized.presentation.homescreen.HomeViewModel
import com.codewithdipesh.habitized.presentation.navigation.HabitizedNavHost
import com.codewithdipesh.habitized.presentation.timerscreen.durationScreen.DurationViewModel
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
                HabitizedNavHost(
                    navController = navController,
                    homeViewModel = homeViewModel,
                    addViewModel = addViewModel,
                    durationViewModel = durationViewModel
                )
            }
        }
    }
}

