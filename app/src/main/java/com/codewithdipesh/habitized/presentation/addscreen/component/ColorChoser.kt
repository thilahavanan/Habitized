package com.codewithdipesh.habitized.presentation.addscreen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ColorChoser(
    modifier: Modifier = Modifier,
    onColorSelected: (Int) -> Unit,
    selectedColor: Int,
    colors: List<Int>
){
    Row(
        modifier = Modifier
            .wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
      colors.forEach {color->
          ColorChoserItem(
              color = color,
              isSelected = color == selectedColor,
              onClick = {
                  onColorSelected(it)
              }
          )
      }
    }
}