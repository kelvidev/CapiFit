package com.fitvara.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitvara.app.R
import com.fitvara.app.ui.theme.Borda
import com.fitvara.app.ui.theme.Creme
import com.fitvara.app.ui.theme.Duna
import com.fitvara.app.ui.theme.Folha
import com.fitvara.app.ui.theme.FolhaEscura
import com.fitvara.app.ui.theme.Sol
import com.fitvara.app.ui.theme.TintaSuave

/** Margem lateral das telas no design (app/assets/). */
private val Margem = 20.dp

/** Itens da barra inferior; [Nenhum] para telas que nao sao destino dela. */
enum class ItemBarra { Nenhum, Historico, Logar, Perfil }

/**
 * Os caminhos que o cabecalho e a barra inferior abrem. Enquanto a navegacao e so
 * troca de estado na MainActivity, passar os callbacks juntos evita repetir cinco
 * parametros em cada tela.
 */
data class Navegacao(
    val aoInicio: () -> Unit = {},
    val aoHistorico: () -> Unit = {},
    val aoLogar: () -> Unit = {},
    val aoPerfil: () -> Unit = {},
    val aoConfiguracoes: () -> Unit = {},
)

/**
 * Esqueleto comum das telas internas: fundo areia, cabecalho, conteudo rolavel e a
 * barra inferior fixa, nas medidas de app/assets/.
 */
@Composable
fun EsqueletoTela(
    navegacao: Navegacao,
    item: ItemBarra,
    modifier: Modifier = Modifier,
    titulo: String? = null,
    subtitulo: String? = null,
    conteudo: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = { BarraNavegacao(navegacao, item) },
    ) { espacamento ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacamento)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Margem),
        ) {
            Spacer(Modifier.height(10.dp))
            CabecalhoTopo(navegacao, titulo, subtitulo)
            conteudo()
            Spacer(Modifier.height(20.dp))
        }
    }
}

/** Titulo opcional a esquerda, casinha e botao "A" a direita. */
@Composable
fun CabecalhoTopo(
    navegacao: Navegacao,
    titulo: String? = null,
    subtitulo: String? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            if (titulo != null) {
                // Os cabecalhos de 02 a 06 usam 18, um ponto abaixo do titleLarge.
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            if (subtitulo != null) {
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        BotaoCabecalho(
            cor = Duna,
            descricao = stringResource(R.string.nav_inicio),
            aoClicar = navegacao.aoInicio,
        ) {
            Canvas(Modifier.size(18.dp)) { iconeCasinha(FolhaEscura) }
        }

        Spacer(Modifier.width(8.dp))

        BotaoCabecalho(
            cor = Sol,
            descricao = stringResource(R.string.nav_configuracoes),
            aoClicar = navegacao.aoConfiguracoes,
        ) {
            Text(
                text = stringResource(R.string.perfil_inicial),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
private fun BotaoCabecalho(
    cor: Color,
    descricao: String,
    aoClicar: () -> Unit,
    conteudo: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(cor)
            .clickable(onClick = aoClicar)
            .semantics { contentDescription = descricao },
        contentAlignment = Alignment.Center,
        content = { conteudo() },
    )
}

/** Barra creme com o botao verde de registrar saltando por cima dela. */
@Composable
fun BarraNavegacao(
    navegacao: Navegacao,
    item: ItemBarra,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .height(94.dp),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(64.dp)
                .background(Creme, RoundedCornerShape(24.dp))
                .border(1.dp, Borda, RoundedCornerShape(24.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ItemNavegacao(
                rotulo = stringResource(R.string.nav_historico),
                selecionado = item == ItemBarra.Historico,
                aoClicar = navegacao.aoHistorico,
                modifier = Modifier.weight(1f),
                icone = { cor -> Canvas(Modifier.size(20.dp)) { iconeRelogio(cor) } },
            )

            Spacer(Modifier.weight(1f))

            ItemNavegacao(
                rotulo = stringResource(R.string.nav_perfil),
                selecionado = item == ItemBarra.Perfil,
                aoClicar = navegacao.aoPerfil,
                modifier = Modifier.weight(1f),
                icone = { cor -> Canvas(Modifier.size(20.dp)) { iconePessoa(cor) } },
            )
        }

        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(Folha)
                    .clickable(onClick = navegacao.aoLogar),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(Modifier.size(20.dp)) {
                    val meio = size.width / 2f
                    drawLine(Creme, Offset(0f, meio), Offset(size.width, meio), 3.dp.toPx(), StrokeCap.Round)
                    drawLine(Creme, Offset(meio, 0f), Offset(meio, size.height), 3.dp.toPx(), StrokeCap.Round)
                }
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.nav_logar),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = FolhaEscura,
            )
        }
    }
}

@Composable
private fun ItemNavegacao(
    rotulo: String,
    selecionado: Boolean,
    aoClicar: () -> Unit,
    icone: @Composable (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    val cor = if (selecionado) FolhaEscura else TintaSuave

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = aoClicar)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        icone(cor)
        Text(
            text = rotulo,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (selecionado) FontWeight.Bold else FontWeight.SemiBold,
            ),
            color = cor,
        )
    }
}

// Os tres icones abaixo sao os tracos do desenho, redesenhados em Canvas para nao
// depender da biblioteca estendida de icones.

private fun DrawScope.iconeCasinha(cor: Color) {
    val u = size.width / 16f
    val traco = Stroke(width = 1.6f * u, cap = StrokeCap.Round, join = StrokeJoin.Round)

    val casa = Path().apply {
        moveTo(0f, 7f * u)
        lineTo(8f * u, 0f)
        lineTo(16f * u, 7f * u)
        lineTo(16f * u, 16f * u)
        lineTo(3f * u, 16f * u)
        lineTo(3f * u, 7f * u)
    }
    val porta = Path().apply {
        moveTo(6f * u, 16f * u)
        lineTo(6f * u, 10f * u)
        lineTo(11f * u, 10f * u)
        lineTo(11f * u, 16f * u)
    }

    drawPath(casa, cor, style = traco)
    drawPath(porta, cor, style = traco)
}

private fun DrawScope.iconeRelogio(cor: Color) {
    val u = size.width / 20f
    val traco = Stroke(width = 1.7f * u, cap = StrokeCap.Round, join = StrokeJoin.Round)

    drawCircle(cor, radius = 8f * u, center = Offset(10f * u, 8f * u), style = traco)

    val ponteiros = Path().apply {
        moveTo(10f * u, 3f * u)
        lineTo(10f * u, 8f * u)
        lineTo(14f * u, 10f * u)
    }
    val volta = Path().apply {
        moveTo(0f, 1f * u)
        lineTo(0f, 7f * u)
        lineTo(6f * u, 7f * u)
    }

    drawPath(ponteiros, cor, style = traco)
    drawPath(volta, cor, style = traco)
}

private fun DrawScope.iconePessoa(cor: Color) {
    val u = size.width / 20f
    val traco = Stroke(width = 1.7f * u, cap = StrokeCap.Round, join = StrokeJoin.Round)

    drawCircle(cor, radius = 4f * u, center = Offset(7f * u, 5f * u), style = traco)

    val ombros = Path().apply {
        moveTo(0f, 18f * u)
        cubicTo(1f * u, 11f * u, 13f * u, 11f * u, 14f * u, 18f * u)
    }
    drawPath(ombros, cor, style = traco)
}
