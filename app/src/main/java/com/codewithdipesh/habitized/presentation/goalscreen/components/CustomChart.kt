package com.codewithdipesh.habitized.presentation.goalscreen.components

import android.R.attr.textColor
import android.graphics.Paint
import android.graphics.Paint.Cap.ROUND
import android.graphics.Path
import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.drawText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.presentation.goalscreen.Effort
import com.codewithdipesh.habitized.presentation.util.toInt
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.io.path.Path
import kotlin.math.min

@Composable
fun CustomChart(
    infos : List<Effort> = emptyList(),
    graphColor : Color,
    textColor : Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier
) {

    val spacing = 100f
    val upperValue = 100
    val lowerValue = 0
    val dummyBarColor = MaterialTheme.colorScheme.scrim.copy(0.5f)

    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = textColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = with(density) { 12.sp.toPx() }
        }
    }
    LaunchedEffect(infos) {
    }

    Box(
        modifier = modifier
        .fillMaxWidth()
        .height(200.dp),
        contentAlignment = Alignment.Center
    ){
        Canvas(
            modifier = Modifier.fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            val spacePerday = min((size.width - spacing) / infos.size,108f)
            val barWidth = kotlin.math.min(spacePerday * 0.6f,65f)
            val chartHeight = size.height - spacing

            // Draw X-axis labels (dates)
            val maxLabels = 5
            val indiceInterval = if (infos.size <= maxLabels) 1 else {
                kotlin.math.max(2, (infos.size - 1) / (maxLabels - 1))
            }
            val indices = if (infos.size <= maxLabels) {
                infos.indices
            } else {
                (0 until infos.size step indiceInterval)// ensure last item is included
            }
            indices.forEach { i ->
                val info = infos[i]
                val date = "${info.day.dayOfMonth} ${
                    info.day.month.name.lowercase().capitalize(Locale.ROOT).take(3)
                }"
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        date,
                        spacing + i * spacePerday + barWidth/2f + 20f,
                        size.height - 5,
                        textPaint
                    )
                }
            }

            // Draw Y-axis labels (effort levels)
            val effortSteps = (upperValue - lowerValue) / 5f

            (0..5).forEach { step ->
                val yPos = chartHeight - (step * chartHeight / 5f) + 20f
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        "${(lowerValue + effortSteps * step).toInt()}%",
                        30f,
                        yPos,
                        textPaint
                    )
                }
            }

            // Draw the chart line and fill (moved outside the Y-axis loop)
            //showing dummy if less than 5
            (0..6).forEachIndexed {index,_ ->
                // Draw bar bg
                val barX = spacing + index * spacePerday + (spacePerday - barWidth) / 2f
                drawRoundRect(
                    color = dummyBarColor,
                    topLeft = Offset(barX, 0f),
                    size = Size(barWidth, chartHeight),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
            }
            //real infos
            infos.forEachIndexed { index, info ->
                val ratio = (info.effortLevel - lowerValue) / (upperValue - lowerValue)
                val barHeight = ratio * chartHeight

                val barX = spacing + index * spacePerday + (spacePerday - barWidth) / 2f
                val barY = chartHeight - barHeight

                // Draw bar bg
                drawRoundRect(
                    color = graphColor.copy(0.3f),
                    topLeft = Offset(barX, 0f),
                    size = Size(barWidth, chartHeight),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
                //actual progress
                drawRoundRect(
                    color = graphColor,
                    topLeft = Offset(barX, barY),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
                )
            }
        }
        //loader

    }
}