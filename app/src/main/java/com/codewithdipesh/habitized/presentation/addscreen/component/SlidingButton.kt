package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.codewithdipesh.habitized.R

@Composable
fun SlidingButton(
    modifier: Modifier = Modifier,
    isSelected : Boolean = false,
    onToggle : () -> Unit
){
    var enabled by remember(isSelected){
        mutableStateOf(isSelected)
    }

    Box(
        modifier = Modifier
            .width(60.dp)
            .wrapContentHeight()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable {
                enabled = !enabled
                onToggle()
            }
    ){
        Row(
            modifier = Modifier.wrapContentSize()
                .padding(4.dp),
        ){
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if(enabled) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface
                    )
            )
            Box(
                modifier = Modifier
                    .size(26.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(
                        if(enabled) Color.White
                        else MaterialTheme.colorScheme.primary
                    )
            )
        }
    }
}