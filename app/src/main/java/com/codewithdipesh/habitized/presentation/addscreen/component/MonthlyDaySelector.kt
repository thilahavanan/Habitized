package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.ui.theme.regular
import kotlin.math.min

@Composable
fun MonthlyDaySelector(
    modifier: Modifier = Modifier,
    selectedDays : List<Int>,
    onDaySelected : (Int)->Unit
){
    Column {
        (0..4).forEach {week->

            val start = ( week * 7 )+1
            val end = min(( week * 7 )+7,31)

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(4.dp),
                horizontalArrangement = if(week == 4) Arrangement.Start else Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                (start..min(end,31)).forEach{day->
                    Box(
                        modifier = Modifier
                            .size(40.dp,40.dp)
                            .clip(CircleShape)
                            .background(
                                if(selectedDays.contains(day)) MaterialTheme.colorScheme.primary
                                else Color.Transparent
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ){
                                onDaySelected(day)
                            },
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = day.toString(),
                            style = TextStyle(
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontFamily = regular,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp
                            )
                        )
                    }
                }
            }
        }
    }
}