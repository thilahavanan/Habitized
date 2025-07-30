package com.codewithdipesh.habitized.widget.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.glance.layout.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.clickable
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.padding
import androidx.glance.layout.wrapContentSize
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.action.actionStartActivity
import androidx.glance.layout.ContentScale
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentHeight
import androidx.glance.unit.ColorProvider
import androidx.glance.unit.Dimension
import com.codewithdipesh.habitized.MainActivity
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.widget.data.HabitWidgetInfo
import com.codewithdipesh.habitized.widget.data.ProgressWidgetData
import com.codewithdipesh.habitized.widget.data.WidgetStatus
import java.time.LocalDate
import kotlin.collections.chunked
import kotlin.collections.forEach

@Composable
fun OverAllHabitWidgetContent(
    habitInfo: HabitWidgetInfo,
    modifier: GlanceModifier = GlanceModifier
){
    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val density = displayMetrics.density

    // Calculate responsive sizes based on device characteristics
    val isLargeScreen = displayMetrics.widthPixels > 1080
    val isHighDensity = density > 2.5f

    // Adaptive sizing
    val titleFontSize = if (isLargeScreen) 20.sp else 16.sp
    val streakFontSize = if (isLargeScreen) 28.sp else 22.sp
    val textFontSize = if (isLargeScreen) 12.sp else 9.sp
    val streakWidth = if (isLargeScreen) 28.dp else 22.dp
    val padding = if (isLargeScreen) 20.dp else 14.dp
    val cellSize = if (isLargeScreen) 22 else 16

    val backgroundColorProvider = when (habitInfo.colorKey) {
        "blue" -> ColorProvider(
            day = Color(0xFF5685A9).copy(0.34f).compositeOver(Color.White),
            night = Color(0XFF010101)
        )
        "green" -> ColorProvider(
            day = Color(0xFF60A487).copy(0.34f).compositeOver(Color.White),
            night = Color(0XFF010101)
        )
        "red" -> ColorProvider(
            day = Color(0xFFA4647B).copy(0.34f).compositeOver(Color.White),
            night = Color(0XFF010101)
        )
        "yellow" -> ColorProvider(
            day = Color(0xFFD48634).copy(0.34f).compositeOver(Color.White),
            night = Color(0XFF010101)
        )
        "purple" -> ColorProvider(
            day = Color(0xFF82729F).copy(0.34f).compositeOver(Color.White),
            night = Color(0XFF010101)
        )
        "see_green" -> ColorProvider(
            day = Color(0xFF44A9B5).copy(0.34f).compositeOver(Color.White),
            night = Color(0XFF010101)
        )
        else -> ColorProvider(
            day = Color(0xFF5685A9).copy(0.34f).compositeOver(Color.White),
            night = Color(0XFF010101)
        )
    }
    val cellColor = when (habitInfo.colorKey) {
        "blue" -> Color(0xFF5685A9)
        "green" -> Color(0xFF60A487)
        "red" -> Color(0xFFA4647B)
        "yellow" -> Color(0xFFD48634)
        "purple" -> Color(0xFF82729F)
        "see_green" -> Color(0xFF44A9B5)
        else -> Color(0xFF5685A9)
    }

    Box(
        modifier = GlanceModifier
            .cornerRadius(20.dp)
            .background(backgroundColorProvider)
            .fillMaxSize()
            .clickable(actionStartActivity<MainActivity>()),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ){
            //title
            Row(
                modifier = GlanceModifier.fillMaxWidth()
            ){
                Text(
                    text = habitInfo.name,
                    style = TextStyle(
                        color = ColorProvider(
                            day = Color.Black,
                            night = Color.White
                        ),
                        fontSize = titleFontSize,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(GlanceModifier.defaultWeight())
                //current streak and icon
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically){
                        Text(
                            text = habitInfo.currentStreak.toString(),
                            style = TextStyle(
                                color = ColorProvider(
                                    day = Color.Black,
                                    night = Color.White
                                ),
                                fontSize = streakFontSize,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        //fire
                        Image(
                            provider = ImageProvider(R.drawable.fire_icon),
                            contentDescription = "Fire icon",
                            contentScale = ContentScale.Fit,
                            modifier = GlanceModifier
                                .size(streakWidth),
                            colorFilter = ColorFilter.tint(ColorProvider(cellColor,cellColor))
                        )
                    }
                    Text(
                        text = "DAYS",
                        style = TextStyle(
                            color = ColorProvider(
                                day = Color(0XFF777777),
                                night = Color(0XFF777777)
                            ),
                            fontSize = textFontSize,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            Spacer(GlanceModifier.height(8.dp))
            //progress
            Progress(
                progress = habitInfo.progress,
                size = 18,
                color = cellColor
            )
        }
    }
}


@Composable
fun Progress(
    modifier: Modifier = Modifier,
    progress: List<ProgressWidgetData>,
    size : Int,
    color :Color
) {
    Row(
        modifier = GlanceModifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalAlignment = Alignment.Top,
    ){
        Box(
            modifier = GlanceModifier
                .wrapContentSize()
        ){
            //1-49
            progress.take(49).let {
                Row {
                    it.chunked(7).forEach { week->
                        Column {
                            week.forEach {day->
                                OverAllCell(
                                    size = size,
                                    color = color,
                                    isSelect = day.status == WidgetStatus.Done,
                                    isLastIndex = false
                                )
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = GlanceModifier
                .wrapContentSize()
        ){
            //49-77
            progress.drop(49).take(28).let {
                Row {
                    it.chunked(7).forEach { week->
                        Column {
                            week.forEach {day->
                                OverAllCell(
                                    size = size,
                                    color = color,
                                    isSelect = day.status == WidgetStatus.Done,
                                    isLastIndex = false
                                )
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = GlanceModifier
                .wrapContentSize()
        ){
            //last 1-7
            progress.drop(77).let {
                Row {
                    it.chunked(7).forEach { week->
                        Column {
                            week.forEach {day->
                                OverAllCell(
                                    size = size,
                                    color = color,
                                    isSelect = day.status == WidgetStatus.Done,
                                    isLastIndex = true
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun OverAllCell(
    modifier: Modifier = Modifier,
    size : Int,
    color: Color,
    isSelect: Boolean,
    isLastIndex : Boolean,
){
    Box(
        modifier = GlanceModifier
            .size(size.dp)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = GlanceModifier
                .size((size - 3).dp)
                .cornerRadius((size/4).dp)
                .background(
                    if (isSelect) color
                    else color.copy(0.34f)
                )
        ) {}
    }
}
