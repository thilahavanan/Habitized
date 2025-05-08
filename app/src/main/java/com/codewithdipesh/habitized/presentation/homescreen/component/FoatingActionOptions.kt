package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun FloatingActionOptions(
    showOptions: Boolean,
    modifier: Modifier = Modifier
){
    Box {
        Column(
            modifier = Modifier.padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedVisibility(
                visible = showOptions,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                Column {
                    AddOptionButton(
                        title = "New Goal",
                        subtitle = "Create personalized goal",
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AddOptionButton(
                        title = "New Habit",
                        subtitle = "Daily actionable task"
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AddOptionButton(
                        title = "New Task",
                        subtitle = "One time task"
                    )
                }
            }
        }
    }

}