package com.fitvara.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * A escala do design (app/assets/) usando a fonte do sistema: Fredoka e DM Sans
 * nao estao no projeto, entao o que se mantem sao os tamanhos e os pesos.
 *
 * Fredoka Semibold -> [Typography.headlineSmall], [Typography.titleLarge],
 * [Typography.titleMedium] e [Typography.titleSmall].
 * DM Sans -> os estilos de corpo e de rotulo.
 */
private val Titulo = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.SemiBold,
)

private val Corpo = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
)

val Typography = Typography(
    // "Bem-vindo de volta!", titulos de tela.
    headlineSmall = Titulo.copy(fontSize = 20.sp, lineHeight = 26.sp),
    // "CapiFIT" no cabecalho.
    titleLarge = Titulo.copy(fontSize = 19.sp, lineHeight = 24.sp),
    // Botao de acao principal.
    titleMedium = Titulo.copy(fontSize = 16.sp, lineHeight = 22.sp),
    // Botao secundario.
    titleSmall = Titulo.copy(fontSize = 14.sp, lineHeight = 20.sp),
    // Texto digitado e placeholders.
    bodyLarge = Corpo.copy(fontSize = 13.sp, lineHeight = 18.sp),
    // Chamadas e textos de apoio.
    bodyMedium = Corpo.copy(fontSize = 12.sp, lineHeight = 17.sp),
    // Tagline, "ou", rodape e mensagens de erro.
    bodySmall = Corpo.copy(fontSize = 11.sp, lineHeight = 15.sp),
    // Rotulos de campo e textos de chip.
    labelLarge = Corpo.copy(fontSize = 12.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium),
    labelMedium = Corpo.copy(fontSize = 11.sp, lineHeight = 15.sp),
    labelSmall = Corpo.copy(fontSize = 10.sp, lineHeight = 14.sp, fontWeight = FontWeight.Medium),
)
