package com.codewithdipesh.habitized.widget.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
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
import java.util.Locale
import kotlin.collections.chunked
import kotlin.collections.forEach

@Composable
fun WeeklyHabitWidgetContent(
    habitInfo: HabitWidgetInfo,
    modifier: GlanceModifier = GlanceModifier
){
    val titleFontSize = 16.sp
    val streakFontSize = 16.sp
    val textFontSize = 8.sp
    val streakWidth = 16.dp
    val padding = 8.dp

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
            .cornerRadius(12.dp)
            .background(backgroundColorProvider)
            .fillMaxSize()
            .clickable(actionStartActivity<MainActivity>()),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .wrapContentHeight()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.Top
        ){
            //title
            Row(
                modifier = GlanceModifier.fillMaxWidth()
            ){
                Column {
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
                    Text(
                        text = "Last 7 days",
                        style = TextStyle(
                            color = ColorProvider(
                                day = Color.Black,
                                night = Color(0XFF777777)
                            ),
                            fontSize = textFontSize,
                            fontWeight = FontWeight.Normal
                        )
                    )
                }
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
            Spacer(GlanceModifier.height(2.dp))
            //progress
            Row {
                habitInfo.progress.forEach { day->
                    WeeklyCell(
                        date = day.date,
                        color = cellColor,
                        size = 30.dp ,
                        textSize = textFontSize.value.toInt(),
                        isSelect = day.status == WidgetStatus.Done,
                        isLastIndex = day == habitInfo.progress.last()
                    )
                }
            }
        }
    }
}


@Composable
fun WeeklyCell(
    modifier: GlanceModifier = GlanceModifier,
    date : LocalDate,
    color: Color,
    size : Dp,
    textSize:Int,
    isLastIndex : Boolean,
    isSelect: Boolean
){
    val bg = if (!isSelect) color.copy(0.15f)
    else color
    Column (
        modifier = GlanceModifier
            .padding(
               end = if (isLastIndex) 0.dp else 14.dp
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box(
            modifier = modifier
                .size(size)
                .cornerRadius(50.dp)
                .background(
                    ColorProvider(
                        day = bg,
                        night = bg
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = date.dayOfMonth.toString(),
                style = TextStyle(
                    color = ColorProvider(
                        day = if(isSelect) Color.White else Color.Black,
                        night = Color.White
                    ),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            )
        }
        Spacer(GlanceModifier.height(2.dp))
        Text(
            text = date.dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
            style = TextStyle(
                color = ColorProvider(
                    day = Color.Black,
                    night = Color.White
                ),
                fontSize = textSize.sp ,
                fontWeight = FontWeight.Normal
            )
        )
    }
}

