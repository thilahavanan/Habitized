package com.codewithdipesh.habitized.ui.theme

import com.codewithdipesh.habitized.R
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val regular = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_light,FontWeight.Light),
    Font(R.font.inter_bold,FontWeight.Bold)
)

val ndot = FontFamily(
    Font(R.font.ndot_mono, FontWeight.Normal)
)

val playfair = FontFamily(
    Font(R.font.playfair_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.playfair_bold, FontWeight.Bold),
    Font(R.font.playfair_normal, FontWeight.Normal)
)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)