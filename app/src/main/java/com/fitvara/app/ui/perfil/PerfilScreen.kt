package com.fitvara.app.ui.perfil

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitvara.app.R
import com.fitvara.app.data.entidades.Atividade
import com.fitvara.app.data.entidades.Dificuldade
import com.fitvara.app.data.entidades.TipoAtividade
import com.fitvara.app.ui.atividade.distanciaTexto
import com.fitvara.app.ui.components.CampoTexto
import com.fitvara.app.ui.inicio.totalKm
import com.fitvara.app.ui.components.EsqueletoTela
import com.fitvara.app.ui.components.ItemBarra
import com.fitvara.app.ui.components.Navegacao
import com.fitvara.app.ui.theme.Barro
import com.fitvara.app.ui.theme.Borda
import com.fitvara.app.ui.theme.Branco
import com.fitvara.app.ui.theme.Creme
import com.fitvara.app.ui.theme.FitvaraTheme
import com.fitvara.app.ui.theme.Folha
import com.fitvara.app.ui.theme.FolhaEscura
import com.fitvara.app.ui.theme.Sol
import com.fitvara.app.ui.theme.Tinta

private val CantoCartao = RoundedCornerShape(16.dp)

private data class Conquista(val titulo: String, val detalhe: String, val cor: Color, val feita: Boolean)

@Composable
fun PerfilScreen(
    nome: String,
    legenda: String,
    atividades: List<Atividade>,
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
    aoSalvarPerfil: (String, String) -> Unit = { _, _ -> },
    aoExcluirConta: () -> Unit = {},
) {
    var editando by remember { mutableStateOf(false) }
    var confirmandoExclusao by remember { mutableStateOf(false) }

    val km = totalKm(atividades)
    val kmInteiro = Math.round(km).toString()
    val primeira = primeiraAtividade(atividades)

    var detalhePrimeira = stringResource(R.string.conquista_primeira_falta)
    if (primeira != null) {
        detalhePrimeira = stringResource(R.string.conquista_primeira_feita, distanciaTexto(primeira.distanciaKm))
    }

    var detalheMes = stringResource(R.string.conquista_5_no_mes_detalhe, atividades.size)
    if (atividades.size == 1) {
        detalheMes = stringResource(R.string.conquista_5_no_mes_detalhe_um)
    }

    val conquistas = listOf(
        Conquista(
            titulo = stringResource(R.string.conquista_primeira),
            detalhe = detalhePrimeira,
            cor = Sol,
            feita = primeira != null,
        ),
        Conquista(
            titulo = stringResource(R.string.conquista_100km),
            detalhe = stringResource(R.string.conquista_100km_detalhe, kmInteiro),
            cor = Folha,
            feita = km >= 100,
        ),
        Conquista(
            titulo = stringResource(R.string.conquista_5_no_mes),
            detalhe = detalheMes,
            cor = Sol,
            feita = maiorQuantidadeNoMes(atividades) >= 5,
        ),
    )

    EsqueletoTela(navegacao = navegacao, item = ItemBarra.Perfil, modifier = modifier) {
        Spacer(Modifier.height(22.dp))

        Avatar()

        Spacer(Modifier.height(14.dp))

        Text(
            text = nome,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Text(
            text = legenda,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )

        Spacer(Modifier.height(26.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            CartaoNumero(atividades.size.toString(), stringResource(R.string.perfil_trilhas), Modifier.weight(1f))
            CartaoNumero(kmInteiro, stringResource(R.string.perfil_km), Modifier.weight(1f))
            CartaoNumero(distanciaTexto(recordeKm(atividades)), stringResource(R.string.perfil_recorde), Modifier.weight(1f))
        }

        Spacer(Modifier.height(26.dp))

        Text(
            text = stringResource(R.string.perfil_conquistas),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp),
        )

        Spacer(Modifier.height(10.dp))

        for (conquista in conquistas) {
            LinhaConquista(conquista)
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(16.dp))

        LinhaAcao(
            icone = R.drawable.ic_lapis,
            texto = stringResource(R.string.perfil_editar),
            acao = stringResource(R.string.perfil_editar_acao),
            corAcao = FolhaEscura,
            aoClicar = { editando = true },
        )

        Spacer(Modifier.height(10.dp))

        LinhaAcao(
            icone = R.drawable.ic_xis,
            texto = stringResource(R.string.perfil_excluir),
            acao = stringResource(R.string.perfil_excluir_acao),
            corAcao = Barro,
            aoClicar = { confirmandoExclusao = true },
        )
    }

    if (editando) {
        DialogoEditar(
            nome = nome,
            legenda = legenda,
            aoFechar = { editando = false },
            aoSalvar = { novoNome, novaLegenda ->
                aoSalvarPerfil(novoNome, novaLegenda)
                editando = false
            },
        )
    }

    if (confirmandoExclusao) {
        AlertDialog(
            onDismissRequest = { confirmandoExclusao = false },
            containerColor = Creme,
            title = { Text(stringResource(R.string.perfil_excluir_titulo)) },
            text = { Text(stringResource(R.string.perfil_excluir_aviso)) },
            confirmButton = {
                TextButton(onClick = { confirmandoExclusao = false; aoExcluirConta() }) {
                    Text(stringResource(R.string.perfil_excluir_acao), color = Barro)
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmandoExclusao = false }) {
                    Text(stringResource(R.string.acao_cancelar))
                }
            },
        )
    }
}

@Composable
private fun ColumnScope.Avatar() {
    Box(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(100.dp)
            .clip(CircleShape)
            .background(Sol),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.capivara_avatar),
            contentDescription = stringResource(R.string.login_mascote),
            tint = Tinta,
            modifier = Modifier.size(76.dp),
        )
    }
}

@Composable
private fun CartaoNumero(valor: String, rotulo: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .height(72.dp)
            .background(Branco.copy(alpha = 0.68f), CantoCartao)
            .border(1.dp, Borda, CantoCartao),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = valor,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = rotulo,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun LinhaConquista(conquista: Conquista) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .background(Branco.copy(alpha = 0.62f), CantoCartao)
            .border(1.dp, Borda, CantoCartao)
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (conquista.feita) conquista.cor else Borda),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_estrela),
                contentDescription = null,
                tint = Creme,
                modifier = Modifier.size(18.dp),
            )
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = conquista.titulo,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = conquista.detalhe,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun LinhaAcao(
    icone: Int,
    texto: String,
    acao: String,
    corAcao: Color,
    aoClicar: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .clip(CantoCartao)
            .background(Branco.copy(alpha = 0.62f))
            .border(1.dp, Borda, CantoCartao)
            .clickable(onClick = aoClicar)
            .padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(icone),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(16.dp),
        )
        Spacer(Modifier.width(10.dp))
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f),
        )
        Text(
            text = acao,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = corAcao,
        )
    }
}

@Composable
private fun DialogoEditar(
    nome: String,
    legenda: String,
    aoFechar: () -> Unit,
    aoSalvar: (String, String) -> Unit,
) {
    var novoNome by rememberSaveable { mutableStateOf(nome) }
    var novaLegenda by rememberSaveable { mutableStateOf(legenda) }

    AlertDialog(
        onDismissRequest = aoFechar,
        containerColor = Creme,
        title = { Text(stringResource(R.string.perfil_editar_titulo)) },
        text = {
            Column {
                CampoTexto(
                    valor = novoNome,
                    aoMudar = { novoNome = it },
                    rotulo = stringResource(R.string.perfil_campo_nome),
                    dica = stringResource(R.string.perfil_campo_nome_dica),
                )
                Spacer(Modifier.height(14.dp))
                CampoTexto(
                    valor = novaLegenda,
                    aoMudar = { novaLegenda = it },
                    rotulo = stringResource(R.string.perfil_campo_legenda),
                    dica = stringResource(R.string.perfil_campo_legenda_dica),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { aoSalvar(novoNome.trim(), novaLegenda.trim()) },
                enabled = novoNome.isNotBlank(),
            ) {
                Text(stringResource(R.string.acao_salvar))
            }
        },
        dismissButton = {
            TextButton(onClick = aoFechar) {
                Text(stringResource(R.string.acao_cancelar))
            }
        },
    )
}

@Preview(widthDp = 390, heightDp = 844, showBackground = true)
@Composable
private fun PerfilPreview() {
    FitvaraTheme {
        PerfilScreen(
            nome = "Ana Souza",
            legenda = "Trilheira desde 2021",
            atividades = listOf(
                Atividade(1, "Trilha do Mirante", TipoAtividade.Trail, "2026-10-26", 150, 5.1, 280, Dificuldade.Media),
                Atividade(2, "Pico da Neblina", TipoAtividade.Mountain, "2026-10-12", 260, 9.4, 640, Dificuldade.Alta),
            ),
            navegacao = Navegacao(),
        )
    }
}
