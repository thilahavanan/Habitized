package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.presentation.util.Days
import com.codewithdipesh.habitized.ui.theme.regular

@Composable
fun WeekDaySelector(
    modifier: Modifier = Modifier,
    daysMap : Map<Days, Boolean>,
    onSelect : (Days)->Unit,
) {

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        daysMap.keys.forEach {day->
            Box(
                modifier = Modifier
                    .size(40.dp,40.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if(daysMap[day] == true) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.surfaceContainerHigh
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ){
                        onSelect(day)
                    },
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = day.toString().take(1),
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontFamily = regular,
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp
                    )
                )
            }
        }
    }

}