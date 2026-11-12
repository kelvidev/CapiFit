package com.fitvara.app.ui.inicio

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitvara.app.R
import com.fitvara.app.data.entidades.Atividade
import com.fitvara.app.data.entidades.Dificuldade
import com.fitvara.app.data.entidades.TipoAtividade
import com.fitvara.app.ui.atividade.distanciaTexto
import com.fitvara.app.ui.atividade.duracaoTexto
import com.fitvara.app.ui.atividade.hojeIso
import com.fitvara.app.ui.components.ChipFiltro
import com.fitvara.app.ui.components.EsqueletoTela
import com.fitvara.app.ui.components.ItemBarra
import com.fitvara.app.ui.components.Navegacao
import com.fitvara.app.ui.theme.Areia
import com.fitvara.app.ui.theme.Borda
import com.fitvara.app.ui.theme.Branco
import com.fitvara.app.ui.theme.ChipCreme
import com.fitvara.app.ui.theme.Creme
import com.fitvara.app.ui.theme.Duna
import com.fitvara.app.ui.theme.FitvaraTheme
import com.fitvara.app.ui.theme.Folha
import com.fitvara.app.ui.theme.Tinta
import com.fitvara.app.ui.theme.TintaSuave

private val AlturaGrafico = 148.dp

@Composable
fun InicioScreen(
    nome: String,
    atividades: List<Atividade>,
    diasDeAcesso: List<String>,
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
    hoje: String = hojeIso(),
) {
    var chip by rememberSaveable { mutableIntStateOf(0) }

    val doTipo = filtrarPorChip(atividades, chip)
    val doPeriodo = atividadesDoPeriodo(doTipo, hoje)
    val sequencia = sequenciaDeDias(diasDeAcesso, hoje)

    val titulo = if (chip == 0) {
        stringResource(R.string.inicio_todos_treinos)
    } else {
        stringResource(R.string.inicio_treinos_de, TipoAtividade.entries[chip - 1].name)
    }
    val selo = if (doPeriodo.size == 1) {
        stringResource(R.string.inicio_uma_atividade)
    } else {
        stringResource(R.string.inicio_atividades, doPeriodo.size)
    }
    val unidadeSequencia = if (sequencia == 1) {
        stringResource(R.string.inicio_sequencia_unidade_um)
    } else {
        stringResource(R.string.inicio_sequencia_unidade)
    }

    EsqueletoTela(
        navegacao = navegacao,
        item = ItemBarra.Nenhum,
        modifier = modifier,
        titulo = stringResource(R.string.inicio_titulo),
        subtitulo = nome,
    ) {
        Spacer(Modifier.height(20.dp))

        Filtros(escolhido = chip, aoEscolher = { chip = it })

        Spacer(Modifier.height(16.dp))

        CartaoProgresso(
            titulo = titulo,
            selo = selo,
            tempo = duracaoTexto(totalMinutos(doPeriodo)),
            distancia = distanciaTexto(totalKm(doPeriodo)) + " km",
            kmPorSemana = kmPorSemana(doPeriodo, hoje),
            meses = mesesDoEixo(hoje),
        )

        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CartaoDestaque(
                rotulo = stringResource(R.string.inicio_sequencia),
                valor = sequencia.toString(),
                unidade = unidadeSequencia,
                fundo = Folha,
                corValor = Creme,
                corTexto = Creme,
                modifier = Modifier.weight(1f),
            )
            CartaoDestaque(
                rotulo = stringResource(R.string.inicio_desnivel),
                valor = desnivelNoMes(doTipo, hoje).toString(),
                unidade = stringResource(R.string.inicio_desnivel_unidade),
                fundo = Duna,
                corValor = Tinta,
                corTexto = TintaSuave,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun Filtros(escolhido: Int, aoEscolher: (Int) -> Unit) {
    val nomes = listOf(
        stringResource(R.string.filtro_todos),
        stringResource(R.string.tipo_run),
        stringResource(R.string.tipo_trail),
        stringResource(R.string.tipo_mountain),
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
            .selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        for (indice in nomes.indices) {
            ChipFiltro(
                texto = nomes[indice],
                selecionado = indice == escolhido,
                aoClicar = { aoEscolher(indice) },
            )
        }
    }
}

@Composable
private fun CartaoProgresso(
    titulo: String,
    selo: String,
    tempo: String,
    distancia: String,
    kmPorSemana: List<Double>,
    meses: List<RotuloMes>,
) {
    val canto = RoundedCornerShape(20.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 420.dp)
            .background(Branco.copy(alpha = 0.66f), canto)
            .border(1.dp, Borda, canto)
            .padding(horizontal = 18.dp, vertical = 16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.inicio_periodo),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = TintaSuave,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 21.sp, lineHeight = 26.sp),
                    color = Tinta,
                )
            }

            Spacer(Modifier.width(8.dp))

            Selo(selo)
        }

        Spacer(Modifier.height(28.dp))

        Row {
            Total(stringResource(R.string.inicio_tempo), tempo, Modifier.weight(1f))
            Total(stringResource(R.string.inicio_distancia), distancia, Modifier.weight(1f))
        }

        Spacer(Modifier.height(34.dp))

        Column(Modifier.padding(end = 32.dp)) {
            Grafico(kmPorSemana)
            Spacer(Modifier.height(22.dp))
            EixoMeses(meses)
        }
    }
}

@Composable
private fun Selo(texto: String) {
    Box(
        modifier = Modifier
            .height(36.dp)
            .background(ChipCreme, CircleShape)
            .border(1.dp, Borda, CircleShape)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = TintaSuave,
        )
    }
}

@Composable
private fun Total(rotulo: String, valor: String, modifier: Modifier = Modifier) {
    Column(modifier) {
        Text(
            text = rotulo,
            style = MaterialTheme.typography.bodySmall,
            color = TintaSuave,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = valor,
            style = MaterialTheme.typography.headlineSmall,
            color = Tinta,
        )
    }
}

@Composable
private fun Grafico(kmPorSemana: List<Double>) {
    val descricao = stringResource(R.string.inicio_grafico)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(AlturaGrafico)
            .semantics { contentDescription = descricao },
    ) {
        val topo = 8.dp.toPx()
        val base = size.height
        val guias = listOf(topo, (topo + base) / 2f, base)
        for (y in guias) {
            drawLine(Borda, Offset(0f, y), Offset(size.width, y), 1.dp.toPx(), StrokeCap.Round)
        }

        var maior = 0.0
        for (km in kmPorSemana) {
            if (km > maior) maior = km
        }

        val passo = size.width / (kmPorSemana.size - 1)
        val pontos = mutableListOf<Offset>()
        for (semana in kmPorSemana.indices) {
            val proporcao = if (maior > 0) kmPorSemana[semana] / maior else 0.0
            val altura = (proporcao * AlturaGrafico.toPx()).toFloat()
            pontos.add(Offset(semana * passo, base - altura))
        }

        val linha = Path()
        for (indice in pontos.indices) {
            val ponto = pontos[indice]
            if (indice == 0) {
                linha.moveTo(ponto.x, ponto.y)
            } else {
                linha.lineTo(ponto.x, ponto.y)
            }
        }

        val area = Path()
        area.addPath(linha)
        area.lineTo(pontos.last().x, base)
        area.lineTo(pontos.first().x, base)
        area.close()

        drawPath(area, Folha.copy(alpha = 0.12f))
        drawPath(
            linha,
            Folha,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
        for (ponto in pontos) {
            drawCircle(Areia, radius = 3.dp.toPx(), center = ponto)
            drawCircle(Folha, radius = 3.dp.toPx(), center = ponto, style = Stroke(2.dp.toPx()))
        }
    }
}

@Composable
private fun EixoMeses(meses: List<RotuloMes>) {
    Layout(
        modifier = Modifier.fillMaxWidth(),
        content = {
            for (rotulo in meses) {
                Text(
                    text = rotulo.nome,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.SemiBold),
                    color = TintaSuave,
                )
            }
        },
    ) { medidas, limites ->
        val largura = limites.maxWidth
        val rotulos = mutableListOf<Placeable>()
        var altura = 0
        for (medida in medidas) {
            val rotulo = medida.measure(limites.copy(minWidth = 0))
            rotulos.add(rotulo)
            if (rotulo.height > altura) {
                altura = rotulo.height
            }
        }

        layout(largura, altura) {
            for (indice in rotulos.indices) {
                val rotulo = rotulos[indice]
                var x = (meses[indice].semana * largura / (SEMANAS - 1).toFloat()).toInt()
                if (x > largura - rotulo.width) {
                    x = largura - rotulo.width
                }
                rotulo.placeRelative(x, 0)
            }
        }
    }
}

@Composable
private fun CartaoDestaque(
    rotulo: String,
    valor: String,
    unidade: String,
    fundo: Color,
    corValor: Color,
    corTexto: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .heightIn(min = 130.dp)
            .background(fundo, RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 18.dp),
    ) {
        Text(
            text = rotulo,
            style = MaterialTheme.typography.labelLarge,
            color = corTexto,
        )
        Spacer(Modifier.height(10.dp))
        Text(
            text = valor,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 36.sp,
                lineHeight = 40.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = corValor,
        )
        Spacer(Modifier.height(2.dp))
        Text(
            text = unidade,
            style = MaterialTheme.typography.bodySmall,
            color = corTexto,
        )
    }
}

@Preview(widthDp = 390, heightDp = 844, showBackground = true)
@Composable
private fun InicioPreview() {
    FitvaraTheme {
        InicioScreen(
            nome = "Ana Souza",
            atividades = ExemploParaPreview,
            diasDeAcesso = listOf("2026-10-26", "2026-10-25"),
            navegacao = Navegacao(),
            hoje = "2026-10-26",
        )
    }
}

private val ExemploParaPreview = listOf(
    Atividade(1, "Trilha do Mirante", TipoAtividade.Trail, "2026-10-26", 150, 5.1, 280, Dificuldade.Media),
    Atividade(2, "Caminho da Serra", TipoAtividade.Run, "2026-10-25", 75, 3.4, 120, Dificuldade.Facil),
    Atividade(3, "Pico da Neblina", TipoAtividade.Mountain, "2026-10-12", 260, 8.7, 640, Dificuldade.Alta),
    Atividade(4, "Trilha das Cachoeiras", TipoAtividade.Trail, "2026-09-18", 165, 6.8, 210, Dificuldade.Media),
    Atividade(5, "Volta no Parque", TipoAtividade.Run, "2026-08-20", 40, 2.0, 0, Dificuldade.Facil),
)
