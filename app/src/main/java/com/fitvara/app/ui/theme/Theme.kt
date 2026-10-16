package com.fitvara.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val Cores = lightColorScheme(
    primary = Folha,
    onPrimary = Creme,
    primaryContainer = Duna,
    onPrimaryContainer = FolhaEscura,
    secondary = Sol,
    onSecondary = Tinta,
    secondaryContainer = ChipCreme,
    onSecondaryContainer = TintaSuave,
    tertiary = Barro,
    onTertiary = Creme,
    background = Areia,
    onBackground = Tinta,
    surface = Branco,
    onSurface = Tinta,
    surfaceVariant = ChipCreme,
    onSurfaceVariant = TintaSuave,
    outline = Borda,
    outlineVariant = Borda,
    error = Barro,
    onError = Creme,
)

/**
 * O tema do CapiFIT. Só existe em claro: o design em app/assets/ nao tem versao
 * escura, e dynamic color sobrescreveria a paleta Trailside Pop.
 */
@Composable
fun FitvaraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Cores,
        typography = Typography,
        content = content,
    )
}
