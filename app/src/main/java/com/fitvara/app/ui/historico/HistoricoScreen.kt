package com.fitvara.app.ui.historico

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitvara.app.R
import com.fitvara.app.ui.components.EsqueletoTela
import com.fitvara.app.ui.components.ItemBarra
import com.fitvara.app.ui.components.Navegacao
import com.fitvara.app.ui.theme.Barro
import com.fitvara.app.ui.theme.BarroClaro
import com.fitvara.app.ui.theme.Borda
import com.fitvara.app.ui.theme.Branco
import com.fitvara.app.ui.theme.Creme
import com.fitvara.app.ui.theme.Duna
import com.fitvara.app.ui.theme.FitvaraTheme
import com.fitvara.app.ui.theme.FolhaClara
import com.fitvara.app.ui.theme.FolhaEscura
import com.fitvara.app.ui.theme.SolEscuro
import com.fitvara.app.ui.theme.Tinta
import com.fitvara.app.ui.theme.TintaSuave

/** Subtitulo fixo do desenho; nao e contado a partir da lista. */
private const val TOTAL = "8 atividades registradas"

private data class Atividade(
    val nome: String,
    val dificuldade: String,
    val corDificuldade: Color,
    val tipoEData: String,
    val metricas: String,
)

/** Os quatro cartoes de app/assets/04-historico.svg, com os textos exatos do desenho. */
private val Atividades = listOf(
    Atividade("Trilha do Mirante", "Média", FolhaEscura, "Trail · 30/09/2026", "5,1 km   ·   280 m   ·   2h 30min"),
    Atividade("Caminho da Serra", "Fácil", SolEscuro, "Run · 27/09/2026", "3,4 km   ·   120 m   ·   1h 15min"),
    Atividade("Pico da Neblina", "Alta", Barro, "Mountain · 22/09/2026", "8,7 km   ·   640 m   ·   4h 20min"),
    Atividade("Trilha das Cachoeiras", "Média", FolhaEscura, "Trail · 18/09/2026", "6,8 km   ·   210 m   ·   2h 45min"),
)

private val CantoCartao = RoundedCornerShape(18.dp)
private val CantoBotao = RoundedCornerShape(10.dp)

/**
 * Tela de historico, desenhada em app/assets/04-historico.svg.
 *
 * So visual por enquanto: a lista e a do desenho. [aoEditar] so navega (a tela de
 * nova atividade abre vazia) e Excluir pede confirmacao, mas o cartao nao sai da lista.
 */
@Composable
fun HistoricoScreen(
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
    aoEditar: () -> Unit = {},
) {
    var confirmandoExclusao by rememberSaveable { mutableStateOf<String?>(null) }

    EsqueletoTela(
        navegacao = navegacao,
        item = ItemBarra.Historico,
        modifier = modifier,
        titulo = stringResource(R.string.historico_titulo),
        subtitulo = TOTAL,
    ) {
        Spacer(Modifier.height(18.dp))

        Atividades.forEach { atividade ->
            CartaoAtividade(
                atividade = atividade,
                aoEditar = aoEditar,
                aoExcluir = { confirmandoExclusao = atividade.nome },
            )
            Spacer(Modifier.height(10.dp))
        }
    }

    confirmandoExclusao?.let { nome ->
        AlertDialog(
            onDismissRequest = { confirmandoExclusao = null },
            containerColor = Creme,
            title = { Text(stringResource(R.string.historico_excluir_titulo)) },
            text = { Text(stringResource(R.string.historico_excluir_aviso, nome)) },
            confirmButton = {
                TextButton(onClick = { confirmandoExclusao = null }) {
                    Text(stringResource(R.string.historico_excluir_acao), color = Barro)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmandoExclusao = null }) {
                    Text(stringResource(R.string.acao_cancelar))
                }
            },
        )
    }
}

/** Cartao de uma atividade: nome e selo, tipo e data, metricas e as duas acoes. */
@Composable
private fun CartaoAtividade(
    atividade: Atividade,
    aoEditar: () -> Unit,
    aoExcluir: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Branco.copy(alpha = 0.68f), CantoCartao)
            .border(1.dp, Borda, CantoCartao)
            .padding(horizontal = 14.dp)
            .padding(top = 13.dp, bottom = 12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = atividade.nome,
                style = MaterialTheme.typography.titleSmall,
                color = Tinta,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Spacer(Modifier.width(8.dp))
            Selo(atividade.dificuldade, atividade.corDificuldade)
        }

        Text(
            text = atividade.tipoEData,
            style = MaterialTheme.typography.bodySmall,
            color = TintaSuave,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = atividade.metricas,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Tinta,
        )

        Spacer(Modifier.height(12.dp))

        HorizontalDivider(thickness = 1.dp, color = Borda)

        Spacer(Modifier.height(11.dp))

        // No desenho o Excluir e um pouco mais largo: 152 contra 162.
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BotaoCartao(
                texto = stringResource(R.string.historico_editar),
                fundo = FolhaClara,
                cor = FolhaEscura,
                aoClicar = aoEditar,
                modifier = Modifier.weight(152f),
            )
            BotaoCartao(
                texto = stringResource(R.string.historico_excluir),
                fundo = BarroClaro,
                cor = Barro,
                aoClicar = aoExcluir,
                modifier = Modifier.weight(162f),
            )
        }
    }
}

/** Selo de dificuldade: pilula duna com o texto na cor do nivel. */
@Composable
private fun Selo(texto: String, cor: Color) {
    Box(
        modifier = Modifier
            .height(24.dp)
            .width(54.dp)
            .background(Duna, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
            color = cor,
            maxLines = 1,
        )
    }
}

@Composable
private fun BotaoCartao(
    texto: String,
    fundo: Color,
    cor: Color,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(31.dp)
            .clip(CantoBotao)
            .background(fundo)
            .clickable(onClick = aoClicar),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 11.sp),
            color = cor,
        )
    }
}

@Preview(widthDp = 390, heightDp = 844, showBackground = true)
@Composable
private fun HistoricoPreview() {
    FitvaraTheme {
        HistoricoScreen(navegacao = Navegacao())
    }
}
