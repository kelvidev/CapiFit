package com.fitvara.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import com.fitvara.app.ui.theme.Capivara
import com.fitvara.app.ui.theme.CapivaraClara
import com.fitvara.app.ui.theme.CapivaraEscura
import com.fitvara.app.ui.theme.CapivaraPata
import com.fitvara.app.ui.theme.Coral
import com.fitvara.app.ui.theme.CoralClaro
import com.fitvara.app.ui.theme.MateEscuro
import com.fitvara.app.ui.theme.Tinta

/** A mascote e desenhada num espaco de 200x200 e escalada para o tamanho do Canvas. */
private const val LADO = 200f

/**
 * A capivara da Fitvara.
 *
 * Ela respira e balanca sozinha. [tapandoOlhos] faz ela cobrir os olhos com as
 * patinhas (usado quando o campo de senha esta em foco), [levantando] poe um
 * halter nas maos, e trocar [comemorarChave] dispara um pulinho.
 */
@Composable
fun CapivaraMascote(
    modifier: Modifier = Modifier,
    levantando: Boolean = false,
    tapandoOlhos: Boolean = false,
    comemorarChave: Int = 0,
) {
    val ciclo = rememberInfiniteTransition(label = "capivara")

    val respiro by ciclo.animateFloat(
        initialValue = 0.985f,
        targetValue = 1.022f,
        animationSpec = infiniteRepeatable(tween(2200, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "respiro",
    )
    val balanco by ciclo.animateFloat(
        initialValue = -5f,
        targetValue = 5f,
        animationSpec = infiniteRepeatable(tween(2700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "balanco",
    )
    // 1 = olho aberto, ~0 = olho fechado. Fica aberto a maior parte do ciclo.
    val piscada by ciclo.animateFloat(
        initialValue = 1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            keyframes {
                durationMillis = 4200
                1f at 0
                1f at 3500
                0.06f at 3620
                0.06f at 3700
                1f at 3840
            },
        ),
        label = "piscada",
    )

    // Vai e volta das repeticoes enquanto ela esta levantando peso.
    val repeticao by ciclo.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(620, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "repeticao",
    )

    val molaPata = spring<Float>(dampingRatio = 0.55f, stiffness = Spring.StiffnessLow)
    val pataY by animateFloatAsState(
        // Levantar peso tem prioridade: se ela esta treinando, nao esta
        // tapando os olhos, mesmo com o campo de senha ainda em foco.
        targetValue = when {
            levantando -> 176f
            tapandoOlhos -> 82f
            else -> 170f
        },
        animationSpec = molaPata,
        label = "pataY",
    )
    val pataX by animateFloatAsState(
        targetValue = when {
            levantando -> 47f
            tapandoOlhos -> 25f
            else -> 47f
        },
        animationSpec = molaPata,
        label = "pataX",
    )
    val halter by animateFloatAsState(
        targetValue = if (levantando) 1f else 0f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "halter",
    )

    val pulo = remember { Animatable(0f) }
    LaunchedEffect(comemorarChave) {
        if (comemorarChave > 0) {
            pulo.snapTo(0f)
            pulo.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 700
                    0f at 0
                    1f at 260 using FastOutSlowInEasing
                    0f at 560
                    0.25f at 640
                    0f at 700
                },
            )
        }
    }

    Canvas(modifier) {
        val k = size.minDimension / LADO
        val sobra = (size.width - LADO * k) / 2f
        withTransform({
            translate(left = sobra, top = 0f)
            scale(k, k, pivot = Offset.Zero)
            translate(left = 0f, top = balanco - 30f * pulo.value)
            // Respira a partir das patas, nao do centro: parece peso no chao.
            scale(respiro, respiro, pivot = Offset(LADO / 2f, 192f))
        }) {
            desenharCapivara(
                piscada = piscada,
                pataY = pataY - repeticao * 8f * halter,
                pataX = pataX,
                halter = halter,
                tapandoOlhos = tapandoOlhos,
            )
        }
    }
}

private fun DrawScope.desenharCapivara(
    piscada: Float,
    pataY: Float,
    pataX: Float,
    halter: Float,
    tapandoOlhos: Boolean,
) {
    val meio = LADO / 2f

    // Sombra no chao.
    drawOval(
        color = Tinta.copy(alpha = 0.08f),
        topLeft = Offset(meio - 56f, 184f),
        size = Size(112f, 16f),
    )

    // Corpo: fica quase todo escondido atras da cabecona.
    drawRoundRectSimples(CapivaraClara, 52f, 148f, 148f, 192f, 28f)

    // Orelhinhas: pequenas, nos cantos de cima. Desenhadas antes da cabeca,
    // entao so a metade de fora aparece - e assim que elas ficam na capivara.
    for (lado in listOf(-1f, 1f)) {
        val cx = meio + lado * 42f
        drawCircle(CapivaraEscura, radius = 11f, center = Offset(cx, 38f))
        drawCircle(CoralClaro, radius = 5f, center = Offset(cx + lado * 2f, 39f))
    }

    // Cabeca: COMPRIDA, nao redonda. A cara da capivara e um bloco alto que
    // desce ate a narina - e esse alongamento que tira o ar de ursinho.
    val cabeca = caminhoArredondado(52f, 30f, 148f, 162f, 40f)
    drawPath(cabeca, Capivara)

    // Faixa de treino, recortada no contorno da cabeca.
    clipPath(cabeca) {
        drawRect(Coral, topLeft = Offset(50f, 46f), size = Size(100f, 18f))
        drawRect(Color.White.copy(alpha = 0.22f), topLeft = Offset(50f, 46f), size = Size(100f, 6f))
    }
    // No da faixa, do lado de fora.
    drawCircle(Coral, radius = 7.5f, center = Offset(147f, 60f))
    drawCircle(Coral, radius = 4f, center = Offset(155f, 54f))

    // Olhos: pequenos, altos e bem afastados - olho de bicho semiaquatico.
    for (lado in listOf(-1f, 1f)) {
        val cx = meio + lado * 25f
        val alturaOlho = 14f * piscada
        drawOval(
            color = Tinta,
            topLeft = Offset(cx - 7f, 80f - alturaOlho / 2f),
            size = Size(14f, alturaOlho),
        )
        if (piscada > 0.5f) {
            drawCircle(
                color = Color.White.copy(alpha = piscada),
                radius = 2.6f,
                center = Offset(cx + 2.5f, 76f),
            )
        }
    }

    // Bochechas, baixas e discretas.
    for (lado in listOf(-1f, 1f)) {
        drawCircle(Coral.copy(alpha = 0.16f), radius = 10f, center = Offset(meio + lado * 36f, 104f))
    }

    // Narina: um bloco escuro largo e achatado la na PONTA do focinho.
    // E o tracinho mais reconhecivel da capivara.
    drawRoundRectSimples(CapivaraEscura, meio - 23f, 116f, meio + 23f, 134f, 8f)
    for (lado in listOf(-1f, 1f)) {
        drawOval(
            color = Tinta.copy(alpha = 0.40f),
            topLeft = Offset(meio + lado * 11f - 4.5f, 121f),
            size = Size(9f, 7f),
        )
    }

    // Filtro e boca, logo abaixo da narina.
    val boca = Path().apply {
        moveTo(meio, 136f)
        lineTo(meio, 142f)
        moveTo(meio, 142f)
        quadraticBezierTo(meio - 7f, 149f, meio - 14f, 145f)
        moveTo(meio, 142f)
        quadraticBezierTo(meio + 7f, 149f, meio + 14f, 145f)
    }
    drawPath(boca, CapivaraEscura, style = Stroke(width = 3f, cap = StrokeCap.Round))
    // Dentinhos de roedor.
    drawRoundRectSimples(Color.White, meio - 7f, 142f, meio - 1f, 150f, 2.5f)
    drawRoundRectSimples(Color.White, meio + 1f, 142f, meio + 7f, 150f, 2.5f)
    // Pintinhas do bigode, dos lados da narina.
    for (lado in listOf(-1f, 1f)) {
        for (i in 0..2) {
            drawCircle(
                color = CapivaraEscura.copy(alpha = 0.35f),
                radius = 1.7f,
                center = Offset(meio + lado * (28f + i * 4f), 126f + i * 6f),
            )
        }
    }

    // Halter, atras das patas.
    if (halter > 0.01f) {
        val y = pataY - 2f
        drawLine(
            color = Tinta.copy(alpha = 0.7f * halter),
            start = Offset(meio - pataX, y),
            end = Offset(meio + pataX, y),
            strokeWidth = 8f,
            cap = StrokeCap.Round,
        )
        for (lado in listOf(-1f, 1f)) {
            val cx = meio + lado * (pataX + 8f)
            drawRoundRectSimples(
                cor = MateEscuro.copy(alpha = halter),
                esquerda = cx - 8f,
                topo = y - 15f,
                direita = cx + 8f,
                base = y + 15f,
                raio = 6f,
            )
        }
    }

    // Patinhas dianteiras: curtas e atarracadas. Tom proprio e contorno, senao
    // somem contra a cabeca na hora de tapar os olhos.
    val raioPata = if (tapandoOlhos && halter < 0.5f) 16f else 13f
    for (lado in listOf(-1f, 1f)) {
        val cx = meio + lado * pataX
        val pata = caminhoArredondado(
            esquerda = cx - raioPata,
            topo = pataY - raioPata,
            direita = cx + raioPata,
            base = pataY + raioPata,
            raio = raioPata - 1f,
        )
        drawPath(pata, CapivaraPata)
        drawPath(pata, CapivaraEscura.copy(alpha = 0.45f), style = Stroke(width = 2.5f))
        // Dedinhos: ajudam a leitura de "pata", inclusive por cima do rosto.
        for (i in -1..1) {
            drawCircle(
                color = CapivaraEscura.copy(alpha = 0.35f),
                radius = 2.4f,
                center = Offset(cx + i * 7f, pataY + raioPata - 6f),
            )
        }
    }
}

/** Retangulo arredondado desenhado por cantos, que e como as medidas acima foram pensadas. */
private fun DrawScope.drawRoundRectSimples(
    cor: Color,
    esquerda: Float,
    topo: Float,
    direita: Float,
    base: Float,
    raio: Float,
) {
    drawPath(caminhoArredondado(esquerda, topo, direita, base, raio), cor)
}

private fun caminhoArredondado(
    esquerda: Float,
    topo: Float,
    direita: Float,
    base: Float,
    raio: Float,
): Path = Path().apply {
    val r = raio.coerceAtMost(minOf(direita - esquerda, base - topo) / 2f)
    moveTo(esquerda + r, topo)
    lineTo(direita - r, topo)
    quadraticBezierTo(direita, topo, direita, topo + r)
    lineTo(direita, base - r)
    quadraticBezierTo(direita, base, direita - r, base)
    lineTo(esquerda + r, base)
    quadraticBezierTo(esquerda, base, esquerda, base - r)
    lineTo(esquerda, topo + r)
    quadraticBezierTo(esquerda, topo, esquerda + r, topo)
    close()
}
