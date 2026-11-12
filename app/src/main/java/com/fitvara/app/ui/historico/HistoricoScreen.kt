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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitvara.app.R
import com.fitvara.app.data.entidades.Atividade
import com.fitvara.app.data.entidades.Dificuldade
import com.fitvara.app.data.entidades.TipoAtividade
import com.fitvara.app.ui.atividade.dataBr
import com.fitvara.app.ui.atividade.distanciaTexto
import com.fitvara.app.ui.atividade.duracaoTexto
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
import com.fitvara.app.ui.theme.Sol
import com.fitvara.app.ui.theme.SolEscuro
import com.fitvara.app.ui.theme.Tinta
import com.fitvara.app.ui.theme.TintaSuave

private val CantoCartao = RoundedCornerShape(18.dp)
private val CantoBotao = RoundedCornerShape(10.dp)

@Composable
fun HistoricoScreen(
    atividades: List<Atividade>,
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
    aoAbrir: (Atividade) -> Unit = {},
    aoFavoritar: (Atividade) -> Unit = {},
    aoEditar: (Atividade) -> Unit = {},
    aoExcluir: (Atividade) -> Unit = {},
) {
    var confirmandoId by rememberSaveable { mutableStateOf<Long?>(null) }

    EsqueletoTela(
        navegacao = navegacao,
        item = ItemBarra.Historico,
        modifier = modifier,
        titulo = stringResource(R.string.historico_titulo),
        subtitulo = when (atividades.size) {
            0 -> stringResource(R.string.historico_nenhuma)
            1 -> stringResource(R.string.historico_uma)
            else -> stringResource(R.string.historico_total, atividades.size)
        },
    ) {
        Spacer(Modifier.height(18.dp))

        if (atividades.isEmpty()) {
            CartaoVazio(aoLogar = navegacao.aoLogar)
        }

        for (atividade in atividades) {
            key(atividade.id) {
                CartaoAtividade(
                    atividade = atividade,
                    aoAbrir = { aoAbrir(atividade) },
                    aoFavoritar = { aoFavoritar(atividade) },
                    aoEditar = { aoEditar(atividade) },
                    aoExcluir = { confirmandoId = atividade.id },
                )
            }
            Spacer(Modifier.height(10.dp))
        }
    }

    var atividade: Atividade? = null
    for (item in atividades) {
        if (item.id == confirmandoId) {
            atividade = item
        }
    }
    if (atividade != null) {
        DialogoExcluir(
            nome = atividade.nome,
            aoConfirmar = {
                confirmandoId = null
                aoExcluir(atividade)
            },
            aoCancelar = { confirmandoId = null },
        )
    }
}

@Composable
fun DialogoExcluir(nome: String, aoConfirmar: () -> Unit, aoCancelar: () -> Unit) {
    AlertDialog(
        onDismissRequest = aoCancelar,
        containerColor = Creme,
        title = { Text(stringResource(R.string.historico_excluir_titulo)) },
        text = { Text(stringResource(R.string.historico_excluir_aviso, nome)) },
        confirmButton = {
            TextButton(onClick = aoConfirmar) {
                Text(stringResource(R.string.historico_excluir_acao), color = Barro)
            }
        },
        dismissButton = {
            TextButton(onClick = aoCancelar) {
                Text(stringResource(R.string.acao_cancelar))
            }
        },
    )
}

@Composable
private fun CartaoAtividade(
    atividade: Atividade,
    aoAbrir: () -> Unit,
    aoFavoritar: () -> Unit,
    aoEditar: () -> Unit,
    aoExcluir: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(CantoCartao)
            .background(Branco.copy(alpha = 0.68f))
            .border(1.dp, Borda, CantoCartao)
            .clickable(onClick = aoAbrir)
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
            BotaoFavorito(favorita = atividade.favorita, aoClicar = aoFavoritar)
            Spacer(Modifier.width(6.dp))
            Selo(nomeDificuldade(atividade.dificuldade), corDificuldade(atividade.dificuldade))
        }

        Text(
            text = "${nomeTipo(atividade.tipo)} · ${dataBr(atividade.data)}",
            style = MaterialTheme.typography.bodySmall,
            color = TintaSuave,
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "${distanciaTexto(atividade.distanciaKm)} km   ·   ${atividade.desnivelM} m   ·   " +
                duracaoTexto(atividade.duracaoMin),
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
            color = Tinta,
        )

        Spacer(Modifier.height(12.dp))

        HorizontalDivider(thickness = 1.dp, color = Borda)

        Spacer(Modifier.height(11.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BotaoCartao(
                icone = R.drawable.ic_lapis,
                texto = stringResource(R.string.historico_editar),
                fundo = FolhaClara,
                cor = FolhaEscura,
                aoClicar = aoEditar,
                modifier = Modifier.weight(152f),
            )
            BotaoCartao(
                icone = R.drawable.ic_xis,
                texto = stringResource(R.string.historico_excluir),
                fundo = BarroClaro,
                cor = Barro,
                aoClicar = aoExcluir,
                modifier = Modifier.weight(162f),
            )
        }
    }
}

@Composable
private fun CartaoVazio(aoLogar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Branco.copy(alpha = 0.68f), CantoCartao)
            .border(1.dp, Borda, CantoCartao)
            .padding(14.dp),
    ) {
        Text(
            text = stringResource(R.string.historico_vazio_titulo),
            style = MaterialTheme.typography.titleSmall,
            color = Tinta,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.historico_vazio_texto),
            style = MaterialTheme.typography.bodyMedium,
            color = TintaSuave,
        )
        Spacer(Modifier.height(12.dp))
        BotaoCartao(
            icone = R.drawable.ic_mais_pequeno,
            texto = stringResource(R.string.historico_vazio_acao),
            fundo = FolhaClara,
            cor = FolhaEscura,
            aoClicar = aoLogar,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun BotaoFavorito(favorita: Boolean, aoClicar: () -> Unit) {
    var icone = R.drawable.ic_estrela_vazia
    var cor = TintaSuave
    var descricao = stringResource(R.string.favorito_marcar)
    if (favorita) {
        icone = R.drawable.ic_estrela
        cor = Sol
        descricao = stringResource(R.string.favorito_desmarcar)
    }

    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .clickable(onClick = aoClicar),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(icone),
            contentDescription = descricao,
            tint = cor,
            modifier = Modifier.size(18.dp),
        )
    }
}

@Composable
fun nomeTipo(tipo: TipoAtividade): String = stringResource(
    when (tipo) {
        TipoAtividade.Run -> R.string.tipo_run
        TipoAtividade.Trail -> R.string.tipo_trail
        TipoAtividade.Mountain -> R.string.tipo_mountain
    },
)

@Composable
fun nomeDificuldade(dificuldade: Dificuldade): String = stringResource(
    when (dificuldade) {
        Dificuldade.Facil -> R.string.dificuldade_facil
        Dificuldade.Media -> R.string.dificuldade_media
        Dificuldade.Alta -> R.string.dificuldade_alta
    },
)

fun corDificuldade(dificuldade: Dificuldade): Color = when (dificuldade) {
    Dificuldade.Facil -> SolEscuro
    Dificuldade.Media -> FolhaEscura
    Dificuldade.Alta -> Barro
}

@Composable
fun Selo(texto: String, cor: Color) {
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
fun BotaoCartao(
    icone: Int,
    texto: String,
    fundo: Color,
    cor: Color,
    aoClicar: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .height(31.dp)
            .clip(CantoBotao)
            .background(fundo)
            .clickable(onClick = aoClicar),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icone),
            contentDescription = null,
            tint = cor,
            modifier = Modifier.size(13.dp),
        )
        Spacer(Modifier.width(6.dp))
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
        HistoricoScreen(atividades = ExemploDoDesenho, navegacao = Navegacao())
    }
}

@Preview(widthDp = 390, heightDp = 844, showBackground = true)
@Composable
private fun HistoricoVazioPreview() {
    FitvaraTheme {
        HistoricoScreen(atividades = emptyList(), navegacao = Navegacao())
    }
}

private val ExemploDoDesenho = listOf(
    Atividade(1, "Trilha do Mirante", TipoAtividade.Trail, "2026-09-30", 150, 5.1, 280, Dificuldade.Media),
    Atividade(2, "Caminho da Serra", TipoAtividade.Run, "2026-09-27", 75, 3.4, 120, Dificuldade.Facil),
    Atividade(3, "Pico da Neblina", TipoAtividade.Mountain, "2026-09-22", 260, 8.7, 640, Dificuldade.Alta),
    Atividade(4, "Trilha das Cachoeiras", TipoAtividade.Trail, "2026-09-18", 165, 6.8, 210, Dificuldade.Media),
)
