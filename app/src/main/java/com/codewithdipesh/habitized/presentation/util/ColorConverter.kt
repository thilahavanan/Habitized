package com.codewithdipesh.habitized.presentation.util

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.glance.unit.ColorProvider
import com.codewithdipesh.habitized.R
import com.codewithdipesh.habitized.ui.theme.LocalCustomColors

fun Int.toColor(): Color = Color(this)
fun Color.toInt(): Int = this.value.toInt()

@Composable
fun getThemedColorFromKey(key: String): Color {
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

@Composable
fun getOriginalColorFromKey(key: String): Color {
    return when (key) {
        "blue" -> colorResource(R.color.blue)
        "green" -> colorResource(R.color.green)
        "red" -> colorResource(R.color.red)
        "yellow" -> colorResource(R.color.yellow)
        "purple" ->colorResource(R.color.purple)
        "see_green" -> colorResource(R.color.see_green)
        else -> colorResource(R.color.yellow)
    }
}


@Composable
fun getAnimatedFireIcon(key : String) : Int {
    return when (key) {
        "blue" -> R.raw.fire_blue
        "green" -> R.raw.fire_green
        "red" -> R.raw.fire_red
        "yellow" -> R.raw.fire_yellow
        "purple" -> R.raw.fire_purple
        "see_green" -> R.raw.fire_see_green
        else -> R.raw.fire_blue
    }
}

