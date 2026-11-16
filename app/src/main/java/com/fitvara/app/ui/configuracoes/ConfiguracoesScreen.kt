package com.fitvara.app.ui.configuracoes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitvara.app.R
import com.fitvara.app.ui.components.CampoTexto
import com.fitvara.app.ui.components.EsqueletoTela
import com.fitvara.app.ui.components.ItemBarra
import com.fitvara.app.ui.components.Navegacao
import com.fitvara.app.ui.theme.Barro
import com.fitvara.app.ui.theme.Borda
import com.fitvara.app.ui.theme.Branco
import com.fitvara.app.ui.theme.Creme
import com.fitvara.app.ui.theme.FitvaraTheme
import com.fitvara.app.ui.theme.Tinta
import com.fitvara.app.ui.theme.TintaSuave
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

private val CantoLinha = RoundedCornerShape(15.dp)

@Composable
fun ConfiguracoesScreen(
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
    aoAbrirSobre: () -> Unit = {},
    aoSalvarPerfil: (String, String) -> Unit = {_, _ -> },
    aoExcluirConta: () -> Unit = {},
    aoSair: () -> Unit = {},
    nome: String,
    legenda: String,
) {
    var editando by remember { mutableStateOf(false) }
    var confirmandoExclusao by remember { mutableStateOf(false) }

    EsqueletoTela(navegacao = navegacao, item = ItemBarra.Nenhum, modifier = modifier) {
        Spacer(Modifier.height(20.dp))

        Text(
            text = stringResource(R.string.config_titulo),
            style = MaterialTheme.typography.headlineSmall,
            color = Tinta,
        )
        Text(
            text = stringResource(R.string.config_subtitulo),
            style = MaterialTheme.typography.bodyMedium,
            color = TintaSuave,
        )

        Secao(stringResource(R.string.config_conta), primeira = true)
        LinhaConfig(stringResource(R.string.perfil_editar), aoClicar = { editando = true })
        LinhaConfig(stringResource(R.string.perfil_excluir), aoClicar = { confirmandoExclusao = true })

        Secao(stringResource(R.string.config_sobre))
        LinhaConfig(stringResource(R.string.sobre_titulo),
            aoClicar = aoAbrirSobre
        )

        Spacer(Modifier.height(8.dp))

        Linha(modifier = Modifier.clickable(onClick = aoSair)) {
            Text(
                text = stringResource(R.string.config_sair),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = Barro,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(R.string.config_sair_acao),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                color = Barro,
                modifier = Modifier.padding(end = 4.dp),
            )
        }
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
private fun Secao(titulo: String, primeira: Boolean = false) {
    Spacer(Modifier.height(if (primeira) 22.dp else 12.dp))
    Text(
        text = titulo,
        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
        color = TintaSuave,
        modifier = Modifier.padding(start = 4.dp),
    )
    Spacer(Modifier.height(2.dp))
}

@Composable
private fun Linha(
    modifier: Modifier = Modifier,
    conteudo: @Composable RowScope.() -> Unit,
) {
    Spacer(Modifier.height(8.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(CantoLinha)
            .background(Branco.copy(alpha = 0.62f))
            .border(1.dp, Borda, CantoLinha)
            .then(modifier)
            .padding(start = 18.dp, end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        content = conteudo,
    )
}

@Composable
private fun LinhaConfig(texto: String, valor: String? = null, aoClicar: () -> Unit = {}) {
    Linha(modifier = Modifier.clickable(onClick = aoClicar)) {
        Text(
            text = texto,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
            color = Tinta,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f),
        )
        Spacer(Modifier.width(8.dp))
        if (valor != null) {
            Text(
                text = valor,
                style = MaterialTheme.typography.bodySmall,
                color = TintaSuave,
                maxLines = 1,
            )
            Spacer(Modifier.width(8.dp))
        }
        Icon(
            painter = painterResource(R.drawable.ic_seta),
            contentDescription = null,
            tint = TintaSuave,
            modifier = Modifier.size(18.dp),
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
private fun ConfiguracoesPreview() {
    FitvaraTheme {
        ConfiguracoesScreen(nome = "teste", legenda = "teste", navegacao = Navegacao())
    }
}
