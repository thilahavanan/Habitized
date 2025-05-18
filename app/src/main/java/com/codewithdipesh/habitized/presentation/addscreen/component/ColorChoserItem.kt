package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp

@Composable
fun ColorChoserItem(
    color : Int ,
    isSelected :  Boolean,
    onClick : (Int) -> Unit,
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(colorResource(id = color))
            .then(
                if(isSelected) Modifier.border(width=2.dp, color = MaterialTheme.colorScheme.onPrimary, shape = CircleShape)
                else Modifier
            )
            .clickable{
                onClick(color)
            }
    )

}