package com.fitvara.app.ui.configuracoes

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
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
import com.fitvara.app.ui.theme.Borda
import com.fitvara.app.ui.theme.Branco
import com.fitvara.app.ui.theme.Creme
import com.fitvara.app.ui.theme.FitvaraTheme
import com.fitvara.app.ui.theme.Folha
import com.fitvara.app.ui.theme.Tinta
import com.fitvara.app.ui.theme.TintaSuave

private val CantoLinha = RoundedCornerShape(15.dp)

/**
 * Tela de configuracoes, desenhada em app/assets/06-configuracoes.svg.
 *
 * So visual por enquanto: os valores a direita sao os do desenho, as linhas com seta
 * nao abrem nada e os switches so mudam a propria aparencia. [aoSair] leva ao login.
 */
@Composable
fun ConfiguracoesScreen(
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
    aoSair: () -> Unit = {},
) {
    // Todos ligados, como no desenho.
    var notificacoes by rememberSaveable { mutableStateOf(true) }
    var vibracao by rememberSaveable { mutableStateOf(true) }
    var gps by rememberSaveable { mutableStateOf(true) }
    var hidratacao by rememberSaveable { mutableStateOf(true) }

    EsqueletoTela(navegacao = navegacao, item = ItemBarra.Nenhum, modifier = modifier) {
        Spacer(Modifier.height(20.dp))

        // Aqui o titulo fica abaixo dos botoes, maior que nas outras telas.
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
        LinhaConfig(stringResource(R.string.config_seguranca), valor = "Google · e-mail")
        LinhaConfig(stringResource(R.string.config_dados_conta))

        Secao(stringResource(R.string.config_preferencias))
        LinhaSwitch(stringResource(R.string.config_notificacoes), notificacoes) { notificacoes = it }
        LinhaSwitch(stringResource(R.string.config_vibracao), vibracao) { vibracao = it }
        LinhaConfig(stringResource(R.string.config_tema), valor = "Claro")
        LinhaConfig(stringResource(R.string.config_unidades), valor = "km · m")
        LinhaConfig(stringResource(R.string.config_idioma), valor = "Português (BR)")

        Secao(stringResource(R.string.config_trilhas))
        LinhaSwitch(stringResource(R.string.config_gps), gps) { gps = it }
        LinhaSwitch(stringResource(R.string.config_hidratacao), hidratacao) { hidratacao = it }

        Secao(stringResource(R.string.config_privacidade))
        LinhaConfig(stringResource(R.string.config_privacidade_dados))
        LinhaConfig(stringResource(R.string.config_exportar))

        Secao(stringResource(R.string.config_sobre))
        LinhaConfig(stringResource(R.string.config_ajuda))
        LinhaConfig(stringResource(R.string.config_termos))
        LinhaConfig(stringResource(R.string.config_versao), valor = "1.0.0")

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
}

/** Rotulo em caixa alta que abre cada grupo de linhas. */
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

/** Caixa clara de 48dp comum a todas as linhas; o espaco de 8dp vem antes dela. */
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

/** Linha com valor opcional a direita e a seta "›". Ainda nao abre nada. */
@Composable
private fun LinhaConfig(texto: String, valor: String? = null) {
    Linha {
        TextoLinha(texto)
        if (valor != null) {
            Text(
                text = valor,
                style = MaterialTheme.typography.bodySmall,
                color = TintaSuave,
                maxLines = 1,
            )
            Spacer(Modifier.width(8.dp))
        }
        Text(
            text = "›",
            style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp, lineHeight = 20.sp),
            color = TintaSuave,
        )
    }
}

/** Linha inteira que liga e desliga o switch. */
@Composable
private fun LinhaSwitch(texto: String, ligado: Boolean, aoMudar: (Boolean) -> Unit) {
    Linha(
        modifier = Modifier.toggleable(value = ligado, role = Role.Switch, onValueChange = aoMudar),
    ) {
        TextoLinha(texto)
        Interruptor(ligado)
    }
}

@Composable
private fun RowScope.TextoLinha(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
        color = Tinta,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.weight(1f),
    )
    Spacer(Modifier.width(8.dp))
}

/**
 * Switch pequeno do desenho (34x20, bolinha de 16). O `Switch` do Material 3 tem
 * 52x32 e fugiria das medidas. Desligado nao aparece no SVG: trilho cor de borda.
 */
@Composable
private fun Interruptor(ligado: Boolean) {
    val trilho by animateColorAsState(if (ligado) Folha else Borda, tween(180), label = "trilho")
    val deslocamento by animateDpAsState(if (ligado) 16.dp else 2.dp, tween(180), label = "bolinha")

    Box(
        modifier = Modifier
            .padding(end = 2.dp)
            .size(width = 34.dp, height = 20.dp)
            .background(trilho, CircleShape),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            Modifier
                .offset(x = deslocamento)
                .size(16.dp)
                .background(Creme, CircleShape),
        )
    }
}

@Preview(widthDp = 390, heightDp = 1210, showBackground = true)
@Composable
private fun ConfiguracoesPreview() {
    FitvaraTheme {
        ConfiguracoesScreen(navegacao = Navegacao())
    }
}
