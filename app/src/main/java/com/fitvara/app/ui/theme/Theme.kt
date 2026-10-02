package com.fitvara.app.ui.theme

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

private val CoresClaras = lightColorScheme(
    primary = MateEscuro,
    onPrimary = Color.White,
    primaryContainer = MateClaro,
    onPrimaryContainer = MateProfundo,
    secondary = Capivara,
    onSecondary = Color.White,
    secondaryContainer = CapivaraClara,
    onSecondaryContainer = CapivaraEscura,
    tertiary = Coral,
    onTertiary = Color.White,
    tertiaryContainer = CoralClaro,
    background = Creme,
    onBackground = Tinta,
    surface = Color.White,
    onSurface = Tinta,
    surfaceVariant = CremeFundo,
    onSurfaceVariant = TintaSuave,
    outline = Color(0xFFD9D3C5),
    outlineVariant = Color(0xFFEAE4D7),
    error = Color(0xFFC2543E),
    onError = Color.White,
)

private val CoresEscuras = darkColorScheme(
    primary = MateNoite,
    onPrimary = MateProfundo,
    primaryContainer = Color(0xFF3C5A2C),
    onPrimaryContainer = MateClaro,
    secondary = CapivaraClara,
    onSecondary = Color(0xFF4A3321),
    tertiary = Coral,
    background = CremeNoite,
    onBackground = TintaNoite,
    surface = CremeNoiteAlto,
    onSurface = TintaNoite,
    surfaceVariant = Color(0xFF38362F),
    onSurfaceVariant = Color(0xFFBDB8AA),
    outline = Color(0xFF55524A),
    error = Color(0xFFE6907C),
)

/**
 * O tema desenhado da Fitvara. [dynamicColor] vem desligado de proposito:
 * a paleta da capivara perde a graca quando o sistema sobrescreve as cores.
 */
@Composable
fun FitvaraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> CoresEscuras
        else -> CoresClaras
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
