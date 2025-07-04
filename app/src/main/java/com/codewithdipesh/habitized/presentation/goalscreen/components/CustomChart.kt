package com.codewithdipesh.habitized.presentation.goalscreen.components

import android.R.attr.textColor
import android.graphics.Paint
import android.graphics.Paint.Cap.ROUND
import android.graphics.Path
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
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
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.presentation.goalscreen.Effort
import com.codewithdipesh.habitized.presentation.util.toInt
import java.util.Locale
import kotlin.io.path.Path
@Composable
fun CustomChart(
    infos : List<Effort> = emptyList(),
    graphColor : Color,
    textColor : Color = MaterialTheme.colorScheme.onPrimary,
    modifier: Modifier = Modifier
) {

    val spacing = 100f
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }
    val upperValue = 100
    val lowerValue = 0

    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = textColor.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = with(density) { 12.sp.toPx() }
        }
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(vertical = 16.dp, horizontal = 8.dp)
    ) {
        val spacePerday = (size.width - spacing) / (infos.size)

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
                    spacing + i * spacePerday,
                    size.height - 5,
                    textPaint
                )
            }
        }

        // Draw Y-axis labels (effort levels)
        val effortSteps = (upperValue - lowerValue) / 5f
        val chartHeight = size.height - spacing
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
        if (infos.isNotEmpty()) {
            var lastX = 0f
            val strokePath = androidx.compose.ui.graphics.Path().apply {
                for (i in infos.indices) {
                    val info = infos[i]
                    val leftRatio = (info.effortLevel - lowerValue) / (upperValue - lowerValue)

                    val x1 = if(infos.lastIndex == i ) size.width else spacing + i * spacePerday
                    val y1 = chartHeight - (leftRatio * chartHeight).toFloat()

                    if (i == 0) {
                        moveTo(x1, y1)
                    }
                    lineTo(x1,y1)
                    lastX = x1
                }
            }

            val fillPath = Path(strokePath.asAndroidPath())
                .asComposePath()
                .apply {
                    lineTo(lastX, size.height - spacing)
                    lineTo(spacing, size.height - spacing)
                    close()
                }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        transparentGraphColor,
                        Color.Transparent
                    ),
                    endY = size.height - spacing
                )
            )
            drawPath(
                path = strokePath,
                color = graphColor,
                style = Stroke(
                    width = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
    }
}