package com.fitvara.app.ui.login

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitvara.app.R
import com.fitvara.app.ui.components.CapivaraMascote
import com.fitvara.app.ui.theme.Capivara
import com.fitvara.app.ui.theme.Ceu
import com.fitvara.app.ui.theme.Coral
import com.fitvara.app.ui.theme.MateClaro
import com.fitvara.app.ui.theme.MateEscuro
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

private val EMAIL = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]{2,}$")

@Composable
fun LoginScreen(modifier: Modifier = Modifier) {
    val escopo = rememberCoroutineScope()
    val avisos = remember { SnackbarHostState() }
    val teclado = LocalSoftwareKeyboardController.current
    val foco = LocalFocusManager.current

    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }
    var lembrar by rememberSaveable { mutableStateOf(true) }

    var carregando by remember { mutableStateOf(false) }
    var erroEmail by remember { mutableStateOf<String?>(null) }
    var erroSenha by remember { mutableStateOf<String?>(null) }
    var senhaEmFoco by remember { mutableStateOf(false) }
    var festa by remember { mutableIntStateOf(0) }

    val msgErroEmail = stringResource(R.string.login_erro_email)
    val msgErroSenha = stringResource(R.string.login_erro_senha)
    val msgBoasVindas = stringResource(R.string.login_boas_vindas)
    val msgRecuperar = stringResource(R.string.login_recuperar_enviado)

    // Tremor horizontal do cartao quando a validacao falha.
    val tremor = remember { Animatable(0f) }

    fun entrar() {
        erroEmail = if (!EMAIL.matches(email.trim())) msgErroEmail else null
        erroSenha = if (senha.length < 6) msgErroSenha else null
        if (erroEmail != null || erroSenha != null) {
            escopo.launch {
                tremor.snapTo(0f)
                tremor.animateTo(1f, tween(520, easing = LinearEasing))
            }
            return
        }
        teclado?.hide()
        foco.clearFocus()
        carregando = true
        escopo.launch {
            delay(1600) // Aqui entra a chamada de autenticacao de verdade.
            carregando = false
            festa++
            avisos.showSnackbar(msgBoasVindas)
        }
    }

    // Entrada escalonada: cada bloco sobe um pouco depois do anterior.
    var montado by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { montado = true }

    Box(modifier.fillMaxSize()) {
        FundoAnimado(Modifier.fillMaxSize())

        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = { SnackbarHost(avisos) },
        ) { espacamento ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(espacamento)
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(12.dp))

                CapivaraMascote(
                    modifier = Modifier
                        .size(206.dp)
                        .entrada(montado, ordem = 0)
                        .semantics {
                            contentDescription = "Mascote da FitVara, uma capivara de faixa de treino"
                        },
                    levantando = carregando,
                    tapandoOlhos = senhaEmFoco && !senhaVisivel,
                    comemorarChave = festa,
                )

                Text(
                    text = stringResource(R.string.login_titulo),
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp,
                        brush = Brush.horizontalGradient(listOf(MateEscuro, Capivara)),
                    ),
                    modifier = Modifier.entrada(montado, ordem = 1),
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.login_subtitulo),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.entrada(montado, ordem = 2),
                )

                Spacer(Modifier.height(26.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .entrada(montado, ordem = 3)
                        .graphicsLayer {
                            val p = tremor.value
                            translationX = sin(p * PI.toFloat() * 5f) * 16f * (1f - p)
                        },
                    shape = RoundedCornerShape(28.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp,
                ) {
                    Column(Modifier.padding(20.dp)) {
                        CampoFitvara(
                            valor = email,
                            aoMudar = { email = it; erroEmail = null },
                            rotulo = stringResource(R.string.login_email),
                            icone = Icons.Filled.Email,
                            erro = erroEmail,
                            tipoTeclado = KeyboardType.Email,
                            acaoIme = ImeAction.Next,
                        )

                        Spacer(Modifier.height(14.dp))

                        CampoFitvara(
                            valor = senha,
                            aoMudar = { senha = it; erroSenha = null },
                            rotulo = stringResource(R.string.login_senha),
                            icone = Icons.Filled.Lock,
                            erro = erroSenha,
                            tipoTeclado = KeyboardType.Password,
                            acaoIme = ImeAction.Done,
                            aoConfirmar = { entrar() },
                            aoMudarFoco = { senhaEmFoco = it },
                            transformacao = if (senhaVisivel) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            acessorio = {
                                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                                    IconeOlho(
                                        aberto = senhaVisivel,
                                        cor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                            },
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                    checked = lembrar,
                                    onCheckedChange = { lembrar = it },
                                    colors = CheckboxDefaults.colors(
                                        checkedColor = MaterialTheme.colorScheme.primary,
                                    ),
                                )
                                Text(
                                    text = stringResource(R.string.login_lembrar),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                            }
                            TextButton(
                                onClick = { escopo.launch { avisos.showSnackbar(msgRecuperar) } },
                            ) {
                                Text(
                                    text = stringResource(R.string.login_esqueci),
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }

                        Spacer(Modifier.height(6.dp))

                        BotaoEntrar(carregando = carregando, aoClicar = ::entrar)
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.entrada(montado, ordem = 4),
                ) {
                    Text(
                        text = stringResource(R.string.login_sem_conta),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    TextButton(onClick = { /* leva para o cadastro */ }) {
                        Text(
                            text = stringResource(R.string.login_criar_conta),
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                            ),
                        )
                    }
                }

                Text(
                    text = stringResource(R.string.login_rodape),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .entrada(montado, ordem = 5),
                )
            }
        }
    }
}

/** Fade + subida, com um atraso proporcional a [ordem]. */
@Composable
private fun Modifier.entrada(montado: Boolean, ordem: Int): Modifier {
    val avanco by animateFloatAsState(
        targetValue = if (montado) 1f else 0f,
        animationSpec = tween(
            durationMillis = 620,
            delayMillis = ordem * 90,
            easing = EaseOutBack,
        ),
        label = "entrada$ordem",
    )
    return graphicsLayer {
        alpha = avanco.coerceIn(0f, 1f)
        translationY = (1f - avanco) * 48.dp.toPx()
    }
}

@Composable
private fun CampoFitvara(
    valor: String,
    aoMudar: (String) -> Unit,
    rotulo: String,
    icone: ImageVector,
    erro: String?,
    tipoTeclado: KeyboardType,
    acaoIme: ImeAction,
    modifier: Modifier = Modifier,
    aoConfirmar: () -> Unit = {},
    aoMudarFoco: (Boolean) -> Unit = {},
    transformacao: VisualTransformation = VisualTransformation.None,
    acessorio: @Composable (() -> Unit)? = null,
) {
    val interacoes = remember { MutableInteractionSource() }
    val emFoco by interacoes.collectIsFocusedAsState()
    LaunchedEffect(emFoco) { aoMudarFoco(emFoco) }

    val corIcone by animateColorAsState(
        targetValue = when {
            erro != null -> MaterialTheme.colorScheme.error
            emFoco -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.onSurfaceVariant
        },
        animationSpec = tween(220),
        label = "corIcone",
    )

    Column(modifier) {
        OutlinedTextField(
            value = valor,
            onValueChange = aoMudar,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(rotulo) },
            singleLine = true,
            isError = erro != null,
            shape = RoundedCornerShape(18.dp),
            interactionSource = interacoes,
            visualTransformation = transformacao,
            leadingIcon = { Icon(icone, contentDescription = null, tint = corIcone) },
            trailingIcon = acessorio,
            keyboardOptions = KeyboardOptions(keyboardType = tipoTeclado, imeAction = acaoIme),
            keyboardActions = KeyboardActions(onDone = { aoConfirmar() }),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MateClaro.copy(alpha = 0.40f),
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color.Transparent,
            ),
        )

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

@Composable
private fun BotaoEntrar(carregando: Boolean, aoClicar: () -> Unit) {
    val interacoes = remember { MutableInteractionSource() }
    val pressionado by interacoes.collectIsPressedAsState()
    val escala by animateFloatAsState(
        targetValue = if (pressionado) 0.96f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "escalaBotao",
    )

    Button(
        onClick = aoClicar,
        enabled = !carregando,
        interactionSource = interacoes,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer { scaleX = escala; scaleY = escala },
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f),
            disabledContentColor = Color.White,
        ),
    ) {
        AnimatedContent(
            targetState = carregando,
            transitionSpec = { fadeIn(tween(180)) togetherWith fadeOut(tween(140)) },
            label = "estadoBotao",
        ) { ocupado ->
            if (ocupado) {
                PontosPulando(Color.White)
            } else {
                Text(
                    text = stringResource(R.string.login_entrar),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                )
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
                    .size(9.dp)
                    .graphicsLayer { translationY = -salto * 7.dp.toPx() },
            ) {
                drawCircle(cor.copy(alpha = 0.55f + salto * 0.45f))
            }
        }
    }
}

/** Olho desenhado a mao: evita puxar a biblioteca de icones estendida so por ele. */
@Composable
private fun IconeOlho(aberto: Boolean, cor: Color) {
    val corte by animateFloatAsState(
        targetValue = if (aberto) 0f else 1f,
        animationSpec = tween(260, easing = FastOutSlowInEasing),
        label = "corte",
    )
    val descricao = stringResource(
        if (aberto) R.string.login_ocultar_senha else R.string.login_mostrar_senha,
    )
    Canvas(
        Modifier
            .size(22.dp)
            .semantics { contentDescription = descricao },
    ) {
        val l = size.width
        val a = size.height
        val contorno = Path().apply {
            moveTo(l * 0.08f, a * 0.5f)
            quadraticBezierTo(l * 0.5f, a * 0.1f, l * 0.92f, a * 0.5f)
            quadraticBezierTo(l * 0.5f, a * 0.9f, l * 0.08f, a * 0.5f)
            close()
        }
        drawPath(contorno, cor, style = Stroke(width = l * 0.085f, cap = StrokeCap.Round))
        drawCircle(cor, radius = l * 0.145f, center = Offset(l * 0.5f, a * 0.5f))
        if (corte > 0.01f) {
            drawLine(
                color = cor,
                start = Offset(l * 0.16f, a * 0.84f),
                end = Offset(l * 0.16f + l * 0.68f * corte, a * 0.84f - a * 0.68f * corte),
                strokeWidth = l * 0.095f,
                cap = StrokeCap.Round,
            )
        }
    }
}

/** Manchas de cor que vagam devagar no fundo. */
@Composable
private fun FundoAnimado(modifier: Modifier) {
    val ciclo = rememberInfiniteTransition(label = "fundo")
    val t by ciclo.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(22000, easing = LinearEasing)),
        label = "deriva",
    )
    val topo = MaterialTheme.colorScheme.background
    val base = MateClaro

    Canvas(modifier) {
        drawRect(Brush.verticalGradient(listOf(topo, base)))

        fun mancha(cor: Color, fracaoX: Float, fracaoY: Float, raio: Float, fase: Float, amplitude: Float) {
            val centro = Offset(
                x = size.width * fracaoX + cos(t + fase) * amplitude,
                y = size.height * fracaoY + sin(t * 0.8f + fase) * amplitude,
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(cor, Color.Transparent),
                    center = centro,
                    radius = raio,
                ),
                radius = raio,
                center = centro,
            )
        }

        mancha(Coral.copy(alpha = 0.16f), 0.12f, 0.14f, size.minDimension * 0.45f, 0f, 36f)
        mancha(Ceu.copy(alpha = 0.34f), 0.92f, 0.22f, size.minDimension * 0.40f, 2.1f, 44f)
        mancha(MateEscuro.copy(alpha = 0.10f), 0.80f, 0.86f, size.minDimension * 0.50f, 4.2f, 40f)
        mancha(Capivara.copy(alpha = 0.12f), 0.08f, 0.72f, size.minDimension * 0.38f, 5.4f, 32f)
    }
}
