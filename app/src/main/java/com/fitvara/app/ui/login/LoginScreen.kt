package com.fitvara.app.ui.login

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitvara.app.R
import com.fitvara.app.ui.components.BotaoPrincipal
import com.fitvara.app.ui.components.CampoTexto
import com.fitvara.app.ui.components.CapivaraMascote
import com.fitvara.app.ui.theme.Borda
import com.fitvara.app.ui.theme.FitvaraTheme
import com.fitvara.app.ui.theme.Sol
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Margem lateral das telas no design (app/assets/). */
private val Margem = 20.dp

/**
 * Tela de login, desenhada em app/assets/01-login.svg.
 *
 * [onEntrar] roda depois da validacao de formato; [onConvidado] entra direto.
 * Os dois levam para o perfil.
 */
@Composable
fun LoginScreen(
    onEntrar: () -> Unit,
    onConvidado: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val escopo = rememberCoroutineScope()
    val teclado = LocalSoftwareKeyboardController.current
    val foco = LocalFocusManager.current

    var email by rememberSaveable { mutableStateOf("") }
    var senha by rememberSaveable { mutableStateOf("") }
    var senhaVisivel by rememberSaveable { mutableStateOf(false) }

    var carregando by remember { mutableStateOf(false) }
    var erroEmail by remember { mutableStateOf<String?>(null) }
    var erroSenha by remember { mutableStateOf<String?>(null) }
    var senhaEmFoco by remember { mutableStateOf(false) }
    var festa by remember { mutableIntStateOf(0) }

    val msgErroEmail = stringResource(R.string.login_erro_email)
    val msgErroSenha = stringResource(R.string.login_erro_senha)

    fun entrar() {
        erroEmail = if (emailValido(email)) null else msgErroEmail
        erroSenha = if (senhaValida(senha)) null else msgErroSenha
        if (erroEmail != null || erroSenha != null) return

        teclado?.hide()
        foco.clearFocus()
        carregando = true
        festa++
        escopo.launch {
            delay(700) // Aqui entra a autenticacao de verdade.
            carregando = false
            onEntrar()
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = {
            Text(
                text = stringResource(R.string.login_rodape),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = Margem, vertical = 12.dp),
            )
        },
    ) { espacamento ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacamento)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Margem),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(8.dp))

            CabecalhoMarca(Modifier.fillMaxWidth())

            Spacer(Modifier.height(44.dp))

            MascoteNoCirculo(
                tapandoOlhos = senhaEmFoco && !senhaVisivel,
                levantando = carregando,
                comemorarChave = festa,
            )

            Spacer(Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.login_bem_vindo),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = stringResource(R.string.login_chamada),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(26.dp))

            CampoTexto(
                valor = email,
                aoMudar = { email = it; erroEmail = null },
                rotulo = stringResource(R.string.login_email),
                dica = stringResource(R.string.login_email_dica),
                erro = erroEmail,
                tipoTeclado = KeyboardType.Email,
                acaoIme = ImeAction.Next,
            )

            Spacer(Modifier.height(14.dp))

            CampoTexto(
                valor = senha,
                aoMudar = { senha = it; erroSenha = null },
                rotulo = stringResource(R.string.login_senha),
                dica = stringResource(R.string.login_senha_dica),
                erro = erroSenha,
                tipoTeclado = KeyboardType.Password,
                acaoIme = ImeAction.Done,
                transformacao = if (senhaVisivel) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                aoConfirmar = { entrar() },
                aoMudarFoco = { senhaEmFoco = it },
                acessorio = {
                    IconButton(
                        onClick = { senhaVisivel = !senhaVisivel },
                        modifier = Modifier.size(36.dp),
                    ) {
                        IconeOlho(
                            aberto = senhaVisivel,
                            cor = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )

            Spacer(Modifier.height(28.dp))

            BotaoPrincipal(
                texto = stringResource(R.string.login_entrar),
                aoClicar = ::entrar,
                carregando = carregando,
            )

            Spacer(Modifier.height(26.dp))

            DivisorOu()

            Spacer(Modifier.height(24.dp))

            BotaoPrincipal(
                texto = stringResource(R.string.login_convidado),
                aoClicar = onConvidado,
                cor = Sol,
                corTexto = MaterialTheme.colorScheme.onBackground,
                altura = 48.dp,
                estilo = MaterialTheme.typography.titleSmall,
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

/** Quadradinho verde com o "C", nome do app e tagline. */
@Composable
private fun CabecalhoMarca(modifier: Modifier = Modifier) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(15.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.app_marca_inicial),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }

        Spacer(Modifier.width(11.dp))

        Column {
            Text(
                text = stringResource(R.string.app_marca),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(R.string.app_tagline),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/** A capivara dentro do circulo Sol, como no desenho. */
@Composable
private fun MascoteNoCirculo(
    tapandoOlhos: Boolean,
    levantando: Boolean,
    comemorarChave: Int,
) {
    val descricao = stringResource(R.string.login_mascote)
    Box(
        modifier = Modifier
            .size(164.dp)
            .clip(CircleShape)
            .background(Sol),
        contentAlignment = Alignment.Center,
    ) {
        CapivaraMascote(
            modifier = Modifier
                .size(148.dp)
                .semantics { contentDescription = descricao },
            levantando = levantando,
            tapandoOlhos = tapandoOlhos,
            comemorarChave = comemorarChave,
        )
    }
}

/** linha —— ou —— linha */
@Composable
private fun DivisorOu() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        HorizontalDivider(Modifier.weight(1f), color = Borda)
        Text(
            text = stringResource(R.string.login_ou),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        HorizontalDivider(Modifier.weight(1f), color = Borda)
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
            .size(20.dp)
            .semantics { contentDescription = descricao },
    ) {
        val l = size.width
        val a = size.height
        val contorno = Path().apply {
            moveTo(l * 0.08f, a * 0.5f)
            quadraticTo(l * 0.5f, a * 0.1f, l * 0.92f, a * 0.5f)
            quadraticTo(l * 0.5f, a * 0.9f, l * 0.08f, a * 0.5f)
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

@Preview(widthDp = 390, heightDp = 844, showBackground = true)
@Composable
private fun LoginPreview() {
    FitvaraTheme {
        LoginScreen(onEntrar = {}, onConvidado = {}, modifier = Modifier.fillMaxSize())
    }
}
