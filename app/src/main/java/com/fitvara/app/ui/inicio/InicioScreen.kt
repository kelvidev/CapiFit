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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitvara.app.R
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

// Textos e numeros fixos, copiados de app/assets/02-inicio-progresso.svg. Nada aqui
// e calculado: os dados de verdade entram com a regra de negocio.
private const val NOME = "Ana Souza"
private const val ATIVIDADES = "8 atividades"
private const val TEMPO = "23h 30min"
private const val DISTANCIA = "52,0 km"
private const val SEQUENCIA = "12"
private const val DESNIVEL = "320"

/** Altura de cada ponto do grafico acima da base, em dp, medida no SVG (base em y=440). */
private val PontosDoGrafico = listOf(7, 20, 10, 52, 30, 90, 75, 110, 45, 128, 80, 148)

/** Altura da area do grafico no desenho: do ponto mais alto (y=292) ate a base (y=440). */
private val AlturaGrafico = 148.dp

/** Meses do eixo e onde cada rotulo comeca (0 = inicio do grafico, 1 = fim). */
private val Meses = listOf("JUL" to 0f, "AGO" to 0.33f, "SET" to 0.68f, "OUT" to 1f)

/**
 * Tela de inicio, desenhada em app/assets/02-inicio-progresso.svg.
 *
 * So visual por enquanto: o chip tocado fica verde, mas os numeros e o grafico sao
 * os do desenho e nao mudam com o filtro.
 */
@Composable
fun InicioScreen(
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
) {
    var chip by rememberSaveable { mutableIntStateOf(0) }

    EsqueletoTela(
        navegacao = navegacao,
        item = ItemBarra.Nenhum,
        modifier = modifier,
        titulo = stringResource(R.string.inicio_titulo),
        subtitulo = NOME,
    ) {
        Spacer(Modifier.height(20.dp))

        Filtros(escolhido = chip, aoEscolher = { chip = it })

        Spacer(Modifier.height(16.dp))

        CartaoProgresso()

        Spacer(Modifier.height(20.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CartaoDestaque(
                rotulo = stringResource(R.string.inicio_sequencia),
                valor = SEQUENCIA,
                unidade = stringResource(R.string.inicio_sequencia_unidade),
                fundo = Folha,
                corValor = Creme,
                corTexto = Creme,
                modifier = Modifier.weight(1f),
            )
            CartaoDestaque(
                rotulo = stringResource(R.string.inicio_desnivel),
                valor = DESNIVEL,
                unidade = stringResource(R.string.inicio_desnivel_unidade),
                fundo = Duna,
                corValor = Tinta,
                corTexto = TintaSuave,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

/** Os quatro chips do topo; rolam de lado se a fonte do aparelho for grande. */
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
        nomes.forEachIndexed { indice, nome ->
            ChipFiltro(
                texto = nome,
                selecionado = indice == escolhido,
                aoClicar = { aoEscolher(indice) },
            )
        }
    }
}

/** O cartao claro com periodo, totais e o grafico das ultimas 12 semanas. */
@Composable
private fun CartaoProgresso() {
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
                    text = stringResource(R.string.inicio_todos_treinos),
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 21.sp, lineHeight = 26.sp),
                    color = Tinta,
                )
            }

            Spacer(Modifier.width(8.dp))

            Selo(ATIVIDADES)
        }

        Spacer(Modifier.height(28.dp))

        Row {
            Total(stringResource(R.string.inicio_tempo), TEMPO, Modifier.weight(1f))
            Total(stringResource(R.string.inicio_distancia), DISTANCIA, Modifier.weight(1f))
        }

        Spacer(Modifier.height(34.dp))

        // O grafico termina 32dp antes da borda direita, como no desenho.
        Column(Modifier.padding(end = 32.dp)) {
            Grafico()
            Spacer(Modifier.height(22.dp))
            EixoMeses()
        }
    }
}

/** Contador no canto do cartao: mesmo visual do chip, mas sem clique. */
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

/** Linha com area preenchida, tres guias e os pontos, nas medidas do desenho. */
@Composable
private fun Grafico() {
    val descricao = stringResource(R.string.inicio_grafico)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(AlturaGrafico)
            .semantics { contentDescription = descricao },
    ) {
        // As guias ficam em y=300, 370 e 440 no SVG: 8dp abaixo do topo, no meio e na base.
        val topo = 8.dp.toPx()
        val base = size.height
        listOf(topo, (topo + base) / 2f, base).forEach { y ->
            drawLine(Borda, Offset(0f, y), Offset(size.width, y), 1.dp.toPx(), StrokeCap.Round)
        }

        val passo = size.width / (PontosDoGrafico.size - 1)
        val pontos = PontosDoGrafico.mapIndexed { indice, altura ->
            Offset(indice * passo, base - altura.dp.toPx())
        }

        val linha = Path().apply {
            pontos.forEachIndexed { indice, p -> if (indice == 0) moveTo(p.x, p.y) else lineTo(p.x, p.y) }
        }
        val area = Path().apply {
            addPath(linha)
            lineTo(pontos.last().x, base)
            lineTo(pontos.first().x, base)
            close()
        }

        drawPath(area, Folha.copy(alpha = 0.12f))
        drawPath(
            linha,
            Folha,
            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
        pontos.forEach { p ->
            drawCircle(Areia, radius = 3.dp.toPx(), center = p)
            drawCircle(Folha, radius = 3.dp.toPx(), center = p, style = Stroke(2.dp.toPx()))
        }
    }
}

/** Rotulos dos meses embaixo do grafico; o ultimo encosta na ponta direita. */
@Composable
private fun EixoMeses() {
    Layout(
        modifier = Modifier.fillMaxWidth(),
        content = {
            Meses.forEach { (mes, _) ->
                Text(
                    text = mes,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.SemiBold),
                    color = TintaSuave,
                )
            }
        },
    ) { medidas, limites ->
        val rotulos = medidas.map { it.measure(limites.copy(minWidth = 0)) }
        val largura = limites.maxWidth
        layout(largura, rotulos.maxOf { it.height }) {
            rotulos.forEachIndexed { indice, rotulo ->
                val x = (Meses[indice].second * largura).toInt()
                rotulo.placeRelative(x.coerceAtMost(largura - rotulo.width), 0)
            }
        }
    }
}

/** Cartao colorido de baixo: rotulo, numero grande e unidade. */
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
        InicioScreen(navegacao = Navegacao())
    }
}
