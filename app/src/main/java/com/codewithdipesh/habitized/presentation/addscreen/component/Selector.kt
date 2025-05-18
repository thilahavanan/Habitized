package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.domain.model.HabitType
import com.codewithdipesh.habitized.ui.theme.ndot

@Composable
fun Selector(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    backgroundColor : Color,
    selectedTextColor : Color,
    nonSelectedTextColor : Color,
    selectedOptionColor : Color,
    nonSelectedOptionColor : Color,
    height : Int = 44,
    shape: Shape = RoundedCornerShape(15.dp)
){
    var option by remember {
        mutableStateOf(selectedOption)
    }
    Box(
        modifier = modifier
            .height(height.dp)
            .clip(shape)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ){
        Row (
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            options.forEach {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .clip(shape)
                        .background(
                            if(it == option ) selectedOptionColor
                            else nonSelectedOptionColor
                        )
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ){
                            option = it
                            onOptionSelected(it)
                        },
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = it,
                        style = TextStyle(
                            color = if(it == option) selectedTextColor
                                    else nonSelectedTextColor,
                            fontFamily = ndot,
                            fontSize = 16.sp
                        )
                    )
                }
            }
        }
    }
}