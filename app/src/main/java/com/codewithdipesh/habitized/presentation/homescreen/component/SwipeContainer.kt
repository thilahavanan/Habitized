package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.compose.ui.graphics.Color
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.codewithdipesh.habitized.domain.model.HabitWithProgress
import com.codewithdipesh.habitized.domain.model.Status
import kotlinx.coroutines.delay

@Composable
fun SwipeContainer(
    item: HabitWithProgress,
    isReminder : Boolean = false,
    onSkip: (HabitWithProgress) -> Unit,
    onDone: (HabitWithProgress) -> Unit,
    onUnSkipDone: (HabitWithProgress) -> Unit,
    animationDuration: Int = 500,
    height : Int,
    content: @Composable (HabitWithProgress) -> Unit,
    modifier : Modifier = Modifier
) {

    var isDone by remember {
        mutableStateOf(false)
    }
    var isSkipped by remember {
        mutableStateOf(false)
    }
    var isUnSkipped by remember {
        mutableStateOf(false)
    }

    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                isSkipped = true
                false
            }else if(value == SwipeToDismissBoxValue.StartToEnd){
                if(item.progress.status == Status.NotStarted){
                    isDone = true
                    true
                }else{
                    isUnSkipped = true
                    true
                }
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
        if(isSkipped) { //show the alertbox
            onSkip(item)
        }
    }
    LaunchedEffect(isUnSkipped) {
        if(isUnSkipped){
            delay(animationDuration.toLong())
            onUnSkipDone(item)
        }
    }

    AnimatedVisibility(
        visible = !(isDone || isUnSkipped),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            enableDismissFromEndToStart = (item.progress.status == Status.NotStarted),
            backgroundContent = {
                val color = if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                    Color.Red.copy(alpha = 0.2f)
                } else if (state.dismissDirection == SwipeToDismissBoxValue.StartToEnd){
                    Color.Green.copy(alpha = 0.2f)
                }
                else Color.Transparent
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(height.dp)
                        .offset(
                            y = if(isReminder) (22).dp else 0.dp
                        )
                        .clip(shape = RoundedCornerShape(15.dp))
                        .background(color)
                        .padding(horizontal = 16.dp),
                    horizontalArrangement =
                        if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                          Arrangement.End
                        }else {
                            Arrangement.Start
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if(item.progress.status == Status.NotStarted){ //show only for undone tasks
                        if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Skip",
                                tint = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                    //show for both
                    if (state.dismissDirection == SwipeToDismissBoxValue.StartToEnd){
                        //tick when its not started/undone
                        //unskip when skipped
                        //undone when done
                        when(item.progress.status){
                            Status.Cancelled -> {
                                Icon(
                                    imageVector =  Icons.Default.Refresh,
                                    contentDescription = "UnSkip",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Status.Done -> {
                                Icon(
                                    imageVector =  Icons.Default.Refresh,
                                    contentDescription = "UnDone",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Status.NotStarted -> {
                                Icon(
                                    imageVector =  Icons.Default.Check,
                                    contentDescription = "Done",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            Status.Ongoing -> {
                                Icon(
                                    imageVector =  Icons.Default.Check,
                                    contentDescription = "Done",
                                    tint = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                    }
                }
            },
            content = {
                Box(
                    modifier = Modifier.graphicsLayer{alpha = 1f}
                ){
                    content(item)
                }
            }
        )
    }
}