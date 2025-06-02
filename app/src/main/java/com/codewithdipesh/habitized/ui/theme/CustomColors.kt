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
    blue = Color(0xFF8DACC3),
    green = Color(0xFF93C0AD),
    red = Color(0xFFC096A5),
    yellow = Color(0xFFD48634),
    purple = Color(0xFFAA9FBD),
    see_green = Color(0xFF81C3CB)
)
val DarkCustomColors = CustomColors(
    blue = Color(0xFF38576E),
    green = Color(0xFF3E6B58),
    red = Color(0xFF6A4150),
    yellow = Color(0xFF9A6B2D),
    purple = Color(0xFF554A68),
    see_green = Color(0xFF2C6D75)
)