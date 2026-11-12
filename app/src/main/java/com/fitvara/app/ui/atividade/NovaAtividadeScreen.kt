package com.fitvara.app.ui.atividade

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitvara.app.R
import com.fitvara.app.data.entidades.Atividade
import com.fitvara.app.data.entidades.Dificuldade
import com.fitvara.app.data.entidades.TipoAtividade
import com.fitvara.app.ui.components.BotaoPrincipal
import com.fitvara.app.ui.components.CampoSelecao
import com.fitvara.app.ui.components.CampoTexto
import com.fitvara.app.ui.components.ChipFiltro
import com.fitvara.app.ui.components.EsqueletoTela
import com.fitvara.app.ui.components.ItemBarra
import com.fitvara.app.ui.components.Navegacao
import com.fitvara.app.ui.theme.Barro
import com.fitvara.app.ui.theme.Creme
import com.fitvara.app.ui.theme.Folha
import com.fitvara.app.ui.theme.FitvaraTheme

private val EntreGrupos = 14.dp

@Composable
fun NovaAtividadeScreen(
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
    inicial: Atividade? = null,
    aoSalvar: (Atividade) -> Unit = {},
) {
    var nome by rememberSaveable { mutableStateOf(inicial?.nome ?: "") }

    var data by rememberSaveable { mutableStateOf(inicial?.data ?: hojeIso()) }
    var duracao by rememberSaveable { mutableStateOf(if (inicial != null) duracaoDigitos(inicial.duracaoMin) else "") }
    var distancia by rememberSaveable { mutableStateOf(if (inicial != null) distanciaTexto(inicial.distanciaKm) else "") }
    var desnivel by rememberSaveable { mutableStateOf(if (inicial != null) inicial.desnivelM.toString() else "") }
    var notas by rememberSaveable { mutableStateOf(inicial?.notas ?: "") }

    var tipo by rememberSaveable { mutableIntStateOf(inicial?.tipo?.ordinal ?: TipoAtividade.Trail.ordinal) }
    var dificuldade by rememberSaveable { mutableIntStateOf(inicial?.dificuldade?.ordinal ?: Dificuldade.Media.ordinal) }
    val editando = inicial != null
    var escolhendoData by rememberSaveable { mutableStateOf(false) }

    var erroNome by remember { mutableStateOf<String?>(null) }
    var erroDuracao by remember { mutableStateOf<String?>(null) }
    var erroDistancia by remember { mutableStateOf<String?>(null) }

    val msgErroNome = stringResource(R.string.atividade_erro_nome)
    val msgErroDuracaoVazia = stringResource(R.string.atividade_erro_duracao_vazia)
    val msgErroDuracaoMinutos = stringResource(R.string.atividade_erro_duracao_minutos)
    val msgErroDistancia = stringResource(R.string.atividade_erro_distancia)

    fun salvar() {
        val minutos = lerDuracao(duracao)
        val km = lerDistancia(distancia)

        erroNome = if (nome.isBlank()) msgErroNome else null
        erroDuracao = when {
            minutos != null -> null
            duracao.isEmpty() -> msgErroDuracaoVazia
            else -> msgErroDuracaoMinutos
        }
        erroDistancia = if (km == null) msgErroDistancia else null
        if (nome.isBlank() || minutos == null || km == null) return

        aoSalvar(
            Atividade(
                id = inicial?.id ?: 0,
                nome = nome.trim(),
                tipo = TipoAtividade.entries[tipo],
                data = data,
                duracaoMin = minutos,
                distanciaKm = km,
                desnivelM = lerDesnivel(desnivel),
                dificuldade = Dificuldade.entries[dificuldade],
                notas = notas.trim(),
            ),
        )
    }

    EsqueletoTela(
        navegacao = navegacao,
        item = ItemBarra.Logar,
        modifier = modifier,
        titulo = stringResource(if (editando) R.string.atividade_editar_titulo else R.string.atividade_titulo),
        subtitulo = stringResource(if (editando) R.string.atividade_editar_subtitulo else R.string.atividade_subtitulo),
    ) {
        Spacer(Modifier.height(18.dp))

        CampoTexto(
            valor = nome,
            aoMudar = { nome = it; erroNome = null },
            rotulo = stringResource(R.string.atividade_nome),
            dica = stringResource(R.string.atividade_nome_dica),
            erro = erroNome,
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
            CampoSelecao(
                valor = dataBr(data),
                rotulo = stringResource(R.string.atividade_data),
                dica = stringResource(R.string.atividade_data_dica),
                aoClicar = { escolhendoData = true },
                modifier = Modifier.weight(1f),
            )
            CampoTexto(
                valor = duracao,
                aoMudar = { duracao = filtrarDuracao(it); erroDuracao = null },
                rotulo = stringResource(R.string.atividade_duracao),
                dica = stringResource(R.string.atividade_duracao_dica),
                erro = erroDuracao,
                tipoTeclado = KeyboardType.Number,
                transformacao = MascaraDuracao,
                modifier = Modifier.weight(1f),
            )
        }

        Spacer(Modifier.height(EntreGrupos))

        Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
            CampoTexto(
                valor = distancia,
                aoMudar = { distancia = filtrarDistancia(it); erroDistancia = null },
                rotulo = stringResource(R.string.atividade_distancia),
                dica = stringResource(R.string.atividade_distancia_dica),
                erro = erroDistancia,
                tipoTeclado = KeyboardType.Decimal,
                modifier = Modifier.weight(1f),
            )
            CampoTexto(
                valor = desnivel,
                aoMudar = { desnivel = filtrarDigitos(it, DIGITOS_DESNIVEL) },
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
            texto = stringResource(if (editando) R.string.atividade_salvar_edicao else R.string.atividade_salvar),
            aoClicar = { salvar() },
            cor = Barro,
            corTexto = Creme,
            altura = 52.dp,
        )
    }

    if (escolhendoData) {
        SeletorData(
            dataIso = data,
            aoEscolher = { data = it },
            aoFechar = { escolhendoData = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SeletorData(
    dataIso: String,
    aoEscolher: (String) -> Unit,
    aoFechar: () -> Unit,
) {
    val hoje = remember { hojeIso() }
    val anoAtual = hoje.take(4).toInt()
    val estado = rememberDatePickerState(
        initialSelectedDateMillis = isoParaMillisUtc(dataIso),
        yearRange = 1900..anoAtual,
        selectableDates = remember(hoje) {
            object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long) = millisUtcParaIso(utcTimeMillis) <= hoje
                override fun isSelectableYear(year: Int) = year <= anoAtual
            }
        },
    )
    val cores = DatePickerDefaults.colors(containerColor = Creme)

    DatePickerDialog(
        onDismissRequest = aoFechar,
        confirmButton = {
            TextButton(
                onClick = {
                    val escolhido = estado.selectedDateMillis
                    if (escolhido != null) {
                        aoEscolher(millisUtcParaIso(escolhido))
                    }
                    aoFechar()
                },
            ) {
                Text(stringResource(R.string.acao_ok), color = Folha)
            }
        },
        dismissButton = {
            TextButton(onClick = aoFechar) {
                Text(stringResource(R.string.acao_cancelar))
            }
        },
        colors = cores,
    ) {
        DatePicker(state = estado, colors = cores, showModeToggle = false)
    }
}

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
        for (indice in opcoes.indices) {
            ChipFiltro(
                texto = opcoes[indice],
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

@Preview(widthDp = 390, heightDp = 844, showBackground = true)
@Composable
private fun EditarAtividadePreview() {
    FitvaraTheme {
        NovaAtividadeScreen(
            navegacao = Navegacao(),
            inicial = Atividade(
                id = 1,
                nome = "Pico da Neblina",
                tipo = TipoAtividade.Mountain,
                data = "2026-09-22",
                duracaoMin = 260,
                distanciaKm = 8.7,
                desnivelM = 640,
                dificuldade = Dificuldade.Alta,
            ),
        )
    }
}
