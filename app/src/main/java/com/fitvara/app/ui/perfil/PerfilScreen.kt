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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitvara.app.R
import com.fitvara.app.ui.components.CampoTexto
import com.fitvara.app.ui.components.CapivaraMascote
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

private val CantoCartao = RoundedCornerShape(16.dp)

/** Numeros e conquistas de exemplo, iguais aos de app/assets/05-perfil.svg. */
private const val TRILHAS = "48"
private const val DISTANCIA = "312"
private const val RECORDE = "9,4"

private data class Conquista(val titulo: String, val detalhe: String, val cor: Color)

private val Conquistas = listOf(
    Conquista("Primeira trilha registrada", "Concluída · 2,1 km", Sol),
    Conquista("100 km acumulados", "312 km no total", Folha),
    Conquista("5 trilhas em um mês", "48 trilhas registradas", Sol),
)

/**
 * Tela de perfil, desenhada em app/assets/05-perfil.svg.
 *
 * Nome e legenda vivem em estado da propria tela: editar muda o que aparece agora e
 * nada e gravado — a regra de negocio entra depois. [aoExcluirConta] e chamado
 * quando o usuario confirma a exclusao.
 */
@Composable
fun PerfilScreen(
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
    aoExcluirConta: () -> Unit = {},
) {
    var nome by rememberSaveable { mutableStateOf("Ana Souza") }
    var legenda by rememberSaveable { mutableStateOf("Trilheira desde 2021") }
    var editando by remember { mutableStateOf(false) }
    var confirmandoExclusao by remember { mutableStateOf(false) }

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
            CartaoNumero(TRILHAS, stringResource(R.string.perfil_trilhas), Modifier.weight(1f))
            CartaoNumero(DISTANCIA, stringResource(R.string.perfil_km), Modifier.weight(1f))
            CartaoNumero(RECORDE, stringResource(R.string.perfil_recorde), Modifier.weight(1f))
        }

        Spacer(Modifier.height(26.dp))

        Text(
            text = stringResource(R.string.perfil_conquistas),
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp),
        )

        Spacer(Modifier.height(10.dp))

        Conquistas.forEach { conquista ->
            LinhaConquista(conquista)
            Spacer(Modifier.height(10.dp))
        }

        Spacer(Modifier.height(16.dp))

        LinhaAcao(
            texto = stringResource(R.string.perfil_editar),
            acao = stringResource(R.string.perfil_editar_acao),
            corAcao = FolhaEscura,
            aoClicar = { editando = true },
        )

        Spacer(Modifier.height(10.dp))

        LinhaAcao(
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
                nome = novoNome
                legenda = novaLegenda
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

/** A capivara no circulo laranja, como no desenho. */
@Composable
private fun ColumnScope.Avatar() {
    val descricao = stringResource(R.string.login_mascote)
    Box(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .size(100.dp)
            .clip(CircleShape)
            .background(Sol),
        contentAlignment = Alignment.Center,
    ) {
        CapivaraMascote(
            modifier = Modifier
                .size(88.dp)
                .semantics { contentDescription = descricao },
        )
    }
}

/** Cartao claro com um numero grande e a unidade embaixo. */
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
                .background(conquista.cor),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "★",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = Creme,
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

/** Linha clicavel com o rotulo da acao na ponta direita. */
@Composable
private fun LinhaAcao(
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

/** Edicao do que aparece no topo da tela. Ainda sem gravar nada. */
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
        PerfilScreen(navegacao = Navegacao())
    }
}
