package com.codewithdipesh.habitized.presentation.homescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.homescreen.component.DatePicker

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewmodel: HomeViewModel
) {

    val state by viewmodel.uiState.collectAsState()

    Scaffold(
        containerColor = colorResource(R.color.background_black)
    ){innerPadding->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ){
            DatePicker(
                currentDate = state.selectedDate,
                onChange = {
                    viewmodel.onDateSelected(it)
                }
            )
        }

    }
}