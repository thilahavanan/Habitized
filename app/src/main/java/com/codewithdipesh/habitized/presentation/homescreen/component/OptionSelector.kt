package com.codewithdipesh.habitized.presentation.homescreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.presentation.homescreen.HomeScreenOption
import com.codewithdipesh.habitized.ui.theme.ndot

@Composable
fun OptionSelector(
    selectedOption: HomeScreenOption,
    onOptionSelected: (HomeScreenOption) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){

        //todos
        Box(
            modifier = Modifier
                .width(screenWidth/2)
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(color =
                    if (selectedOption == HomeScreenOption.TODOS) Color.White
                    else colorResource(R.color.secondary_gray),
                    shape = RoundedCornerShape(50.dp)
                )
                .clickable{
                    onOptionSelected(HomeScreenOption.TODOS)
                },
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Todos",
                style = TextStyle(
                    color = if (selectedOption == HomeScreenOption.TODOS) colorResource(R.color.black)
                            else colorResource(R.color.white),
                    fontSize = 18.sp,
                    fontFamily = ndot
                )
            )
        }
        Spacer(Modifier.width(16.dp))

        //Reminder
        Box(
            modifier = Modifier
                .width(screenWidth/2)
                .weight(1f)
                .height(40.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(color =
                    if (selectedOption == HomeScreenOption.REMINDER) Color.White
                    else colorResource(R.color.secondary_gray),
                    shape = RoundedCornerShape(50.dp)
                )
                .clickable{
                    onOptionSelected(HomeScreenOption.REMINDER)
                },
            contentAlignment = Alignment.Center
        ){
            Text(
                text = "Reminder",
                style = TextStyle(
                    color = if (selectedOption == HomeScreenOption.REMINDER) colorResource(R.color.black)
                    else colorResource(R.color.white),
                    fontSize = 18.sp,
                    fontFamily = ndot
                )
            )
        }

    }


}