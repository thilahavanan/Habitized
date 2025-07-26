package com.codewithdipesh.habitized.widget.elements

import androidx.compose.runtime.Composable
import androidx.glance.layout.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import androidx.glance.layout.width
import com.codewithdipesh.habitized.MainActivity
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.util.getOriginalColorFromKey
import com.codewithdipesh.habitized.widget.data.HabitWidgetInfo

@Composable
fun WeeklyHabitWidgetContent(
    habitInfo: HabitWidgetInfo,
    modifier: GlanceModifier = GlanceModifier
){
    // Adaptive sizing
    val size = LocalSize.current
    val isCompact = size.width <= 250.dp
    val titleFontSize = if (isCompact) 16.sp else 16.sp
    val textFontSize = if (isCompact) 10.sp else 10.sp
    val streakFontSize = if (isCompact) 20.sp else 18.sp
    val streakWidth = if (isCompact) 15.dp else 17.dp
    val spacing = if (isCompact) 3.dp else 7.dp

    Box(
        modifier = GlanceModifier
            .cornerRadius(15.dp)
            .background(
                ColorProvider(
                    day = getOriginalColorFromKey(habitInfo.colorKey)
                        .copy(0.34f)
                        .compositeOver(Color.White),
                    night = Color(0XFF010101)
                )
            )
            .clickable(actionStartActivity<MainActivity>()),
        contentAlignment = Alignment.TopStart
    ) {
        Column(
            modifier = GlanceModifier.wrapContentSize()
                .padding(16.dp)
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
                //current streak and icon
                Column {
                    Row {
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
                            modifier = GlanceModifier
                                .width(streakWidth)
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
            Spacer(GlanceModifier.height(spacing * 2))
            //progress
        }
    }
}