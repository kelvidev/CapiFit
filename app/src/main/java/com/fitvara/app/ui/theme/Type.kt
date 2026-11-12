package com.fitvara.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val Titulo = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
)

private val Corpo = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
)

val Typography = Typography(

    headlineSmall = Titulo.copy(fontSize = 20.sp, lineHeight = 26.sp),

    titleLarge = Titulo.copy(fontSize = 19.sp, lineHeight = 24.sp),

    titleMedium = Titulo.copy(fontSize = 16.sp, lineHeight = 22.sp),

    titleSmall = Titulo.copy(fontSize = 14.sp, lineHeight = 20.sp),

    bodyLarge = Corpo.copy(fontSize = 13.sp, lineHeight = 18.sp),

    bodyMedium = Corpo.copy(fontSize = 12.sp, lineHeight = 17.sp),

    bodySmall = Corpo.copy(fontSize = 11.sp, lineHeight = 15.sp),

    labelLarge = Corpo.copy(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
    labelMedium = Corpo.copy(fontSize = 11.sp, lineHeight = 15.sp),
    labelSmall = Corpo.copy(fontSize = 10.sp, lineHeight = 14.sp, fontWeight = FontWeight.Medium),
)
