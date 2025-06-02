package com.codewithdipesh.habitized.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.codewithdipesh.habitized.ui.theme.LocalCustomColors

fun Int.toColor(): Color = Color(this)
fun Color.toInt(): Int = this.value.toInt()

@Composable
fun getColorFromKey(key: String): Color {
    val colors = LocalCustomColors.current
    return when (key) {
        "blue" -> colors.blue
        "green" -> colors.green
        "red" -> colors.red
        "yellow" -> colors.yellow
        "purple" -> colors.purple
        "see_green" -> colors.see_green
        else -> colors.yellow
    }
}
