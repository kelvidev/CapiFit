package com.fitvara.app.ui.historico

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import com.fitvara.app.ui.theme.FitvaraTheme
import com.fitvara.app.ui.theme.FolhaClara
import com.fitvara.app.ui.theme.FolhaEscura
import com.fitvara.app.ui.theme.Tinta
import com.fitvara.app.ui.theme.TintaSuave

private val CantoCartao = RoundedCornerShape(16.dp)

@Composable
fun DetalheAtividadeScreen(
    atividade: Atividade,
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
    aoFavoritar: () -> Unit = {},
    aoEditar: () -> Unit = {},
    aoExcluir: () -> Unit = {},
) {
    var confirmando by rememberSaveable { mutableStateOf(false) }

    EsqueletoTela(
        navegacao = navegacao,
        item = ItemBarra.Historico,
        modifier = modifier,
        titulo = atividade.nome,
        subtitulo = nomeTipo(atividade.tipo) + " · " + dataBr(atividade.data),
    ) {
        Spacer(Modifier.height(18.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Selo(nomeDificuldade(atividade.dificuldade), corDificuldade(atividade.dificuldade))
            Spacer(Modifier.weight(1f))
            var textoFavorito = stringResource(R.string.favorito_marcar)
            if (atividade.favorita) {
                textoFavorito = stringResource(R.string.favorito_marcada)
            }
            Text(
                text = textoFavorito,
                style = MaterialTheme.typography.bodySmall,
                color = TintaSuave,
            )
            BotaoFavorito(favorita = atividade.favorita, aoClicar = aoFavoritar)
        }

        Spacer(Modifier.height(12.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CartaoInfo(
                rotulo = stringResource(R.string.detalhe_distancia),
                valor = distanciaTexto(atividade.distanciaKm) + " km",
                modifier = Modifier.weight(1f),
            )
            CartaoInfo(
                rotulo = stringResource(R.string.detalhe_duracao),
                valor = duracaoTexto(atividade.duracaoMin),
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(10.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CartaoInfo(
                rotulo = stringResource(R.string.detalhe_desnivel),
                valor = atividade.desnivelM.toString() + " m",
                modifier = Modifier.weight(1f),
            )
            CartaoInfo(
                rotulo = stringResource(R.string.detalhe_data),
                valor = dataBr(atividade.data),
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(10.dp))

        var notas = atividade.notas
        if (notas.isBlank()) {
            notas = stringResource(R.string.detalhe_sem_notas)
        }
        CartaoInfo(
            rotulo = stringResource(R.string.detalhe_notas),
            valor = notas,
            destaque = false,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(Modifier.height(18.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            BotaoCartao(
                icone = R.drawable.ic_lapis,
                texto = stringResource(R.string.historico_editar),
                fundo = FolhaClara,
                cor = FolhaEscura,
                aoClicar = aoEditar,
                modifier = Modifier.weight(1f),
            )
            BotaoCartao(
                icone = R.drawable.ic_xis,
                texto = stringResource(R.string.historico_excluir),
                fundo = BarroClaro,
                cor = Barro,
                aoClicar = { confirmando = true },
                modifier = Modifier.weight(1f),
            )
        }
    }

    if (confirmando) {
        DialogoExcluir(
            nome = atividade.nome,
            aoConfirmar = {
                confirmando = false
                aoExcluir()
            },
            aoCancelar = { confirmando = false },
        )
    }
}

@Composable
private fun CartaoInfo(
    rotulo: String,
    valor: String,
    modifier: Modifier = Modifier,
    destaque: Boolean = true,
) {
    var estilo = MaterialTheme.typography.bodyLarge
    if (destaque) {
        estilo = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
    }

    Column(
        modifier = modifier
            .background(Branco.copy(alpha = 0.68f), CantoCartao)
            .border(1.dp, Borda, CantoCartao)
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Text(
            text = rotulo,
            style = MaterialTheme.typography.bodySmall,
            color = TintaSuave,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = valor,
            style = estilo,
            color = Tinta,
        )
    }
}

@Preview(widthDp = 390, heightDp = 844, showBackground = true)
@Composable
private fun DetalhePreview() {
    FitvaraTheme {
        DetalheAtividadeScreen(
            atividade = Atividade(
                id = 1,
                nome = "Trilha do Mirante",
                tipo = TipoAtividade.Trail,
                data = "2026-09-30",
                duracaoMin = 150,
                distanciaKm = 5.1,
                desnivelM = 280,
                dificuldade = Dificuldade.Media,
                notas = "Subida forte no começo, vista linda no topo.",
                favorita = true,
            ),
            navegacao = Navegacao(),
        )
    }
}
