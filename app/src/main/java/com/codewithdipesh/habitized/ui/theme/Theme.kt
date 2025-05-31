package com.codewithdipesh.habitized.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import com.codewithdipesh.habitized.R

private val DarkColors = darkColorScheme(
    background = Color(0XFF010101),
    surface = Color(0xFF1A1A1A),
    onSurface = Color(0xFF5F5F5F),
    primary = Color(0xFFEEA445),
    onPrimary = Color.White,
    secondary = Color(0xFF1A1A1A) ,
    onSecondary = Color(0xFF5F5F5F),
    tertiary = Color(0xFF7C7878),
    onTertiary = Color.White,
    outline = Color(0xFF2F2E2E),
    surfaceVariant = Color(0xFF2F2E2E),
    inverseOnSurface = Color.Black,
    surfaceContainerHigh = Color(0xFF4E4E4E),
    surfaceDim = Color.White,
    outlineVariant = Color.Black
)

private val LightColors = lightColorScheme(
    background = Color(0xFFF7F7F7),
    surface = Color(0xFFEDEEF2),
    onSurface = Color(0xFF828388),
    primary = Color(0xFFEEA445),
    onPrimary = Color.Black,
    secondary = Color.White ,
    onSecondary = Color.Black,
    tertiary = Color.White,
    onTertiary = Color.Black,
    outline = Color(0xFFD9D9D9),
    surfaceVariant = Color(0xFFD9D9D9),
    inverseOnSurface = Color.White,
    surfaceContainerHigh = Color(0xFFC7C7C7),
    surfaceDim = Color(0xFF7C7878),
    outlineVariant = Color.Black
)

@Composable
fun HabitizedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}