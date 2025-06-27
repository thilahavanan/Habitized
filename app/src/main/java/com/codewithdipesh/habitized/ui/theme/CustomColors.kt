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
    blue = Color(0xFFACC3D5),
    green = Color(0xFFB3D3C6),
    red = Color(0xFFD5B3C1),
    yellow = Color(0xFFECC69F),
    purple = Color(0xFFC7BED3),
    see_green = Color(0xFFA3D4DB)
)
val DarkCustomColors = CustomColors(
    blue = Color(0xFF263C4B),
    green = Color(0xFF284438),
    red = Color(0xFF472C36),
    yellow = Color(0xFF5E3C17),
    purple = Color(0xFF3F2F59),
    see_green = Color(0xFF205A59)
)