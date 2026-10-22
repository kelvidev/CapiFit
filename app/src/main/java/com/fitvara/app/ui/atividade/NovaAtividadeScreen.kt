package com.fitvara.app.ui.atividade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitvara.app.R
import com.fitvara.app.ui.components.BotaoPrincipal
import com.fitvara.app.ui.components.CampoTexto
import com.fitvara.app.ui.components.ChipFiltro
import com.fitvara.app.ui.components.EsqueletoTela
import com.fitvara.app.ui.components.ItemBarra
import com.fitvara.app.ui.components.Navegacao
import com.fitvara.app.ui.theme.Barro
import com.fitvara.app.ui.theme.Creme
import com.fitvara.app.ui.theme.FitvaraTheme

/** Espaco entre um grupo do formulario e o rotulo do proximo, medido no desenho. */
private val EntreGrupos = 14.dp

/**
 * Tela de nova atividade, desenhada em app/assets/03-nova-atividade.svg.
 *
 * So visual por enquanto: os campos aceitam digitacao e os chips mudam o destaque,
 * mas nada e validado nem gravado. [aoSalvar] so leva para o historico.
 */
@Composable
fun NovaAtividadeScreen(
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
    aoSalvar: () -> Unit = {},
) {
    var nome by rememberSaveable { mutableStateOf("") }
    var data by rememberSaveable { mutableStateOf("") }
    var duracao by rememberSaveable { mutableStateOf("") }
    var distancia by rememberSaveable { mutableStateOf("") }
    var desnivel by rememberSaveable { mutableStateOf("") }
    var notas by rememberSaveable { mutableStateOf("") }
    // Selecionados como no desenho: Trail e Media.
    var tipo by rememberSaveable { mutableIntStateOf(1) }
    var dificuldade by rememberSaveable { mutableIntStateOf(1) }

    EsqueletoTela(
        navegacao = navegacao,
        item = ItemBarra.Logar,
        modifier = modifier,
        titulo = stringResource(R.string.atividade_titulo),
        subtitulo = stringResource(R.string.atividade_subtitulo),
    ) {
        Spacer(Modifier.height(18.dp))

        CampoTexto(
            valor = nome,
            aoMudar = { nome = it },
            rotulo = stringResource(R.string.atividade_nome),
            dica = stringResource(R.string.atividade_nome_dica),
        )

        Spacer(Modifier.height(EntreGrupos))

        GrupoChips(
            rotulo = stringResource(R.string.atividade_tipo),
            opcoes = listOf(
                stringResource(R.string.tipo_run),
                stringResource(R.string.tipo_trail),
                stringResource(R.string.tipo_mountain),
            ),
            escolhido = tipo,
            aoEscolher = { tipo = it },
        )

        Spacer(Modifier.height(EntreGrupos))

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            CampoTexto(
                valor = data,
                aoMudar = { data = it },
                rotulo = stringResource(R.string.atividade_data),
                dica = stringResource(R.string.atividade_data_dica),
                tipoTeclado = KeyboardType.Number,
                modifier = Modifier.weight(1f),
            )
            CampoTexto(
                valor = duracao,
                aoMudar = { duracao = it },
                rotulo = stringResource(R.string.atividade_duracao),
                dica = stringResource(R.string.atividade_duracao_dica),
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(EntreGrupos))

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            CampoTexto(
                valor = distancia,
                aoMudar = { distancia = it },
                rotulo = stringResource(R.string.atividade_distancia),
                dica = stringResource(R.string.atividade_distancia_dica),
                tipoTeclado = KeyboardType.Decimal,
                modifier = Modifier.weight(1f),
            )
            CampoTexto(
                valor = desnivel,
                aoMudar = { desnivel = it },
                rotulo = stringResource(R.string.atividade_desnivel),
                dica = stringResource(R.string.atividade_desnivel_dica),
                tipoTeclado = KeyboardType.Number,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(EntreGrupos + 2.dp))

        GrupoChips(
            rotulo = stringResource(R.string.atividade_dificuldade),
            opcoes = listOf(
                stringResource(R.string.dificuldade_facil),
                stringResource(R.string.dificuldade_media),
                stringResource(R.string.dificuldade_alta),
            ),
            escolhido = dificuldade,
            aoEscolher = { dificuldade = it },
        )

        Spacer(Modifier.height(EntreGrupos))

        CampoTexto(
            valor = notas,
            aoMudar = { notas = it },
            rotulo = stringResource(R.string.atividade_notas),
            dica = stringResource(R.string.atividade_notas_dica),
            acaoIme = ImeAction.Default,
            multilinha = true,
        )

        Spacer(Modifier.height(20.dp))

        BotaoPrincipal(
            texto = stringResource(R.string.atividade_salvar),
            aoClicar = aoSalvar,
            cor = Barro,
            corTexto = Creme,
            altura = 52.dp,
        )
    }
}

/** Rotulo e tres chips de mesma largura, como Tipo de treino e Dificuldade. */
@Composable
private fun GrupoChips(
    rotulo: String,
    opcoes: List<String>,
    escolhido: Int,
    aoEscolher: (Int) -> Unit,
) {
    Text(
        text = rotulo,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    Spacer(Modifier.height(6.dp))

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        opcoes.forEachIndexed { indice, opcao ->
            ChipFiltro(
                texto = opcao,
                selecionado = indice == escolhido,
                aoClicar = { aoEscolher(indice) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview(widthDp = 390, heightDp = 844, showBackground = true)
@Composable
private fun NovaAtividadePreview() {
    FitvaraTheme {
        NovaAtividadeScreen(navegacao = Navegacao())
    }
}
