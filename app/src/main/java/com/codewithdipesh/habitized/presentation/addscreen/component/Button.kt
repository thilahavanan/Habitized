package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Button(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    bgColor : Color,
    height:Int = 40,
    content: @Composable () -> Unit
){
    Box(
        modifier = modifier
            .height(height.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(bgColor)
            .clickable{
                if(enabled){
                    onClick()
                }
            },
        contentAlignment = Alignment.Center
    ){
        content()
    }
}