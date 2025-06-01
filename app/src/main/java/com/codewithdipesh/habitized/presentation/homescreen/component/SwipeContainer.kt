package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.compose.ui.graphics.Color
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun <T> SwipeContainer(
    item: T,
    onSkip: (T) -> Unit,
    onDone: (T) -> Unit,
    animationDuration: Int = 500,
    content: @Composable (T) -> Unit
) {

    var isDone by remember {
        mutableStateOf(false)
    }

    var isSkipped by remember {
        mutableStateOf(false)
    }

    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isSkipped = true
                true
            }else if(value == SwipeToDismissBoxValue.StartToEnd){
                isDone = true
                true
            }
            else {
                false
            }
        }
    )

    LaunchedEffect(isDone) {
        if(isDone) {
            delay(animationDuration.toLong())
            onDone(item)
        }
    }

    LaunchedEffect(isSkipped) {
        if(isSkipped) {
            delay(animationDuration.toLong())
            onSkip(item)
        }
    }

    AnimatedVisibility(
        visible = !(isDone || isSkipped),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector =  Icons.Default.Check,
                        contentDescription = "Done",
                        tint = Color.Green.copy(alpha = 0.5f)
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Skip",
                        tint = Color.Red.copy(alpha = 0.5f)
                    )
                }
            },
            content = { content(item) }
        )
    }
}