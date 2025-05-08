package com.codewithdipesh.habitized.presentation.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.homescreen.component.AddOptionButton
import com.codewithdipesh.habitized.presentation.homescreen.component.BottomNavBar
import com.codewithdipesh.habitized.presentation.homescreen.component.DatePicker
import com.codewithdipesh.habitized.presentation.homescreen.component.FloatingActionOptions
import com.codewithdipesh.habitized.presentation.homescreen.component.OptionSelector
import com.codewithdipesh.habitized.presentation.navigation.Screen

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewmodel: HomeViewModel
) {

    val state by viewmodel.uiState.collectAsState()
    var showOptions by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = colorResource(R.color.background_black),
        bottomBar = {
            BottomNavBar(
                selectedScreen = Screen.Home,
                modifier = Modifier,
                isShowingAddOption = showOptions,
                onAddClick = {showOptions= !showOptions}
            )
        },
        floatingActionButton = {
            FloatingActionOptions(showOptions)
        },
        floatingActionButtonPosition = FabPosition.Center
    ){innerPadding->

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .then(
                if(showOptions) Modifier.blur(16.dp)
                 else Modifier
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ){
            DatePicker(
                currentDate = state.selectedDate,
                onChange = {
                    viewmodel.onDateSelected(it)
                }
            )
            Spacer(Modifier.height(16.dp))
            OptionSelector(
                selectedOption = state.selectedOption,
                onOptionSelected = {viewmodel.onOptionSelected(it)}
            )
        }

    }
}