package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codewithdipesh.habitized.ui.theme.ndot

@Composable
fun AddScreenTopBar(
    onBackClick: () -> Unit,
    text : String,
    modifier: Modifier = Modifier
){
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(100.dp)
    ){
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = "back",
                tint = Color.White
            )
        }

        Text(
            text = text,
            style = TextStyle(
                color = Color.White,
                fontFamily = ndot,
                fontSize = 22.sp
            ),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}