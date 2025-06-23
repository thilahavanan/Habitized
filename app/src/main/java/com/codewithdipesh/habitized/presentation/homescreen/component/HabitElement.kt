package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.ui.theme.playfair
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HabitElement(
    modifier: Modifier = Modifier,
    reminder : LocalTime? = null,
    color : androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.secondary,
    isDone : Boolean = false,
    element : @Composable () -> Unit,
) {


    Column(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement =  Arrangement.Center
    ){
        reminder?.let {
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ){
                //icon
                Icon(
                    painter = painterResource(R.drawable.clock),
                    contentDescription = "reminder",
                    tint = if(!isDone) MaterialTheme.colorScheme.surfaceDim
                    else MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.4f)
                )
                //time
                Text(
                    text = reminder.format(
                        DateTimeFormatter.ofPattern("hh:mm a")
                    ),
                    style = TextStyle(
                        color = if(!isDone) MaterialTheme.colorScheme.surfaceDim
                        else MaterialTheme.colorScheme.surfaceDim.copy(alpha = 0.4f),
                        fontFamily = playfair,
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 12.sp
                    )
                )
            }
            Spacer(Modifier.height(8.dp))
        }
        Box(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color)
                .then(
                    if(isDone) Modifier.background(MaterialTheme.colorScheme.background.copy(0.3f))
                    else Modifier
                )
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ){
            element()
        }
    }


}