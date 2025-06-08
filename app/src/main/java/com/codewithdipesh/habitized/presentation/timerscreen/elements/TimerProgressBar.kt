package com.codewithdipesh.habitized.presentation.timerscreen.elements

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun TimerProgressBar(
    progress: Float,
    total : Float,
    modifier: Modifier = Modifier
) {
    val progressFraction by animateFloatAsState(
        targetValue = (progress / total)
    )
    Box(
        modifier = modifier
            .height(16.dp)
            .width(250.dp)
    ) {
        // Background Line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(5.dp)
                .align(Alignment.CenterStart)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
        )

        // Progress Line
        Box(
            modifier = Modifier
                .fillMaxWidth(progressFraction)
                .height(5.dp)
                .align(Alignment.CenterStart)
                .background(MaterialTheme.colorScheme.onPrimary)
        )

        // Moving Dot
        Box(
            modifier = Modifier
                .size(16.dp)
                .offset(x = (progressFraction * 250).dp - 2.dp)
                .background(MaterialTheme.colorScheme.onPrimary, CircleShape)
                .align(Alignment.CenterStart)
        )
    }
}