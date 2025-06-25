package com.codewithdipesh.habitized.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class CustomColors(
    val blue : Color,
    val green : Color,
    val red : Color,
    val yellow : Color,
    val purple : Color,
    val see_green : Color,
)

val LocalCustomColors = staticCompositionLocalOf<CustomColors>{ LightCustomColors }

val LightCustomColors = CustomColors(
    blue = Color(0xFF9EC2DC),
    green = Color(0xFFAADEC8),
    red = Color(0xFFDEADBE),
    yellow = Color(0xFFDA9A55),
    purple = Color(0xFFC5B8DA),
    see_green = Color(0xFF92DCE5)
)
val DarkCustomColors = CustomColors(
    blue = Color(0xFF38576E),
    green = Color(0xFF3E6B58),
    red = Color(0xFF6A4150),
    yellow = Color(0xDA9A6B2D),
    purple = Color(0xFF554A68),
    see_green = Color(0xFF2C6D75)
)