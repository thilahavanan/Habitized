package com.codewithdipesh.habitized.presentation.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.codewithdipesh.habitized.presentation.progress.components.OverallProgress
import com.codewithdipesh.habitized.presentation.progress.components.WeeklyProgress
import kotlinx.coroutines.launch

@Composable
fun ProgressScreen(
    modifier: Modifier = Modifier,
    viewmodel: ProgressViewModel,
    navController: NavController
) {
    val state by viewmodel.state.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            viewmodel.getHabitProgresses()
        }
    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        state.habits.forEach {
            OverallProgress(
                overallRange = state.OverAllDateRange,
                habit = it.habit,
                progresses = it.OverallProgresses
            )
        }
    }

}