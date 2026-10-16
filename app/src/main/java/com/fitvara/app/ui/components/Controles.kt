package com.fitvara.app.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fitvara.app.ui.theme.Borda
import com.fitvara.app.ui.theme.Branco
import com.fitvara.app.ui.theme.TintaFantasma

/** Altura dos campos no design: app/assets/01-login.svg e 03-nova-atividade.svg. */
private val AlturaCampo = 46.dp
private val CantoCampo = RoundedCornerShape(16.dp)

/**
 * Campo de texto do CapiFIT: rotulo acima, caixa branca de 46dp com borda fina.
 *
 * E montado sobre [BasicTextField] porque o `OutlinedTextField` do Material 3 tem
 * altura minima de 56dp e rotulo flutuante — os dois fogem do design.
 */
@Composable
fun CampoTexto(
    valor: String,
    aoMudar: (String) -> Unit,
    rotulo: String,
    dica: String,
    modifier: Modifier = Modifier,
    erro: String? = null,
    tipoTeclado: KeyboardType = KeyboardType.Text,
    acaoIme: ImeAction = ImeAction.Next,
    transformacao: VisualTransformation = VisualTransformation.None,
    aoConfirmar: () -> Unit = {},
    aoMudarFoco: (Boolean) -> Unit = {},
    acessorio: @Composable (() -> Unit)? = null,
) {
    val interacoes = remember { MutableInteractionSource() }
    val emFoco by interacoes.collectIsFocusedAsState()
    LaunchedEffect(emFoco) { aoMudarFoco(emFoco) }

    val contorno = when {
        erro != null -> MaterialTheme.colorScheme.error
        emFoco -> MaterialTheme.colorScheme.primary
        else -> Borda
    }

    Column(modifier) {
        Text(
            text = rotulo,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        Spacer(Modifier.height(6.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(AlturaCampo)
                .background(Branco, CantoCampo)
                .border(1.dp, contorno, CantoCampo)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = valor,
                onValueChange = aoMudar,
                modifier = Modifier.weight(1f),
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
                interactionSource = interacoes,
                visualTransformation = transformacao,
                keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado, imeAction = acaoIme),
                keyboardActions = KeyboardActions(onDone = { aoConfirmar() }),
                decorationBox = { campo ->
                    Box(contentAlignment = Alignment.CenterStart) {
                        if (valor.isEmpty()) {
                            Text(
                                text = dica,
                                style = MaterialTheme.typography.bodyLarge,
                                color = TintaFantasma,
                            )
                        }
                        campo()
                    }
                },
            )

            acessorio?.invoke()
        }

        AnimatedVisibility(
            visible = erro != null,
            enter = fadeIn(tween(180)) + expandVertically(tween(180)),
            exit = fadeOut(tween(140)) + shrinkVertically(tween(140)),
        ) {
            Text(
                text = erro.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 6.dp),
            )
        }
    }
}

/**
 * Botao chapado de canto 16dp, como o "Entrar" e o "Salvar atividade" do design.
 * Afunda um pouco ao ser pressionado e troca o texto por pontinhos em [carregando].
 */
@Composable
fun BotaoPrincipal(
    texto: String,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
    cor: Color = MaterialTheme.colorScheme.primary,
    corTexto: Color = MaterialTheme.colorScheme.onPrimary,
    altura: Dp = 50.dp,
    estilo: TextStyle = MaterialTheme.typography.titleMedium,
    carregando: Boolean = false,
) {
    val interacoes = remember { MutableInteractionSource() }
    val pressionado by interacoes.collectIsPressedAsState()
    val escala by animateFloatAsState(
        targetValue = if (pressionado) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "escalaBotao",
    )

    Button(
        onClick = aoClicar,
        enabled = !carregando,
        interactionSource = interacoes,
        modifier = modifier
            .fillMaxWidth()
            .height(altura)
            .graphicsLayer { scaleX = escala; scaleY = escala },
        shape = RoundedCornerShape(16.dp),
        elevation = null,
        colors = ButtonDefaults.buttonColors(
            containerColor = cor,
            contentColor = corTexto,
            disabledContainerColor = cor,
            disabledContentColor = corTexto,
        ),
    ) {
        AnimatedContent(
            targetState = carregando,
            transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(140)) },
            label = "estadoBotao",
        ) { ocupado ->
            if (ocupado) {
                PontosPulando(corTexto)
            } else {
                Text(text = texto, style = estilo)
            }
        }
    }
}

/** Tres pontinhos quicando, no lugar do spinner padrao. */
@Composable
private fun PontosPulando(cor: Color) {
    val ciclo = rememberInfiniteTransition(label = "pontos")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        repeat(3) { indice ->
            val salto by ciclo.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(460, delayMillis = indice * 130, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse,
                ),
                label = "ponto$indice",
            )
            Canvas(
                Modifier
                    .size(8.dp)
                    .graphicsLayer { translationY = -salto * 6.dp.toPx() },
            ) {
                drawCircle(cor.copy(alpha = 0.55f + salto * 0.45f))
            }
        }
    }
}
