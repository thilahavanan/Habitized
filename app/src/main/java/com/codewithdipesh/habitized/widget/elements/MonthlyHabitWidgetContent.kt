package com.codewithdipesh.habitized.widget.elements

import android.content.Context
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
import androidx.glance.unit.ColorProvider
import com.codewithdipesh.habitized.MainActivity
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.util.getOriginalColorFromKey
import com.codewithdipesh.habitized.widget.data.HabitWidgetInfo

@Composable
fun OverAllHabitWidgetContent(
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

    Box(
        modifier = GlanceModifier
            .cornerRadius(15.dp)
            .background(backgroundColorProvider)
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
