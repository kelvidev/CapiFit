package com.fitvara.app.ui.configuracoes

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitvara.app.BuildConfig
import com.fitvara.app.R
import com.fitvara.app.ui.components.EsqueletoTela
import com.fitvara.app.ui.components.ItemBarra
import com.fitvara.app.ui.components.Navegacao
import com.fitvara.app.ui.theme.Barro
import com.fitvara.app.ui.theme.Borda
import com.fitvara.app.ui.theme.Branco
import com.fitvara.app.ui.theme.FitvaraTheme
import com.fitvara.app.ui.theme.Tinta
import com.fitvara.app.ui.theme.TintaSuave

private val CantoLinha = RoundedCornerShape(15.dp)

@Composable
fun ConfiguracoesScreen(
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
    aoAbrirSobre: () -> Unit = {},
    aoSair: () -> Unit = {},
) {
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
        LinhaConfig(stringResource(R.string.config_dados_conta))

        Secao(stringResource(R.string.config_sobre))
        LinhaConfig(stringResource(R.string.config_ajuda))
        LinhaConfig(stringResource(R.string.config_termos))
        LinhaConfig(stringResource(R.string.config_versao), valor = BuildConfig.VERSION_NAME, aoClicar = aoAbrirSobre)
        LinhaConfig(
            texto = stringResource(R.string.config_creditos),
            valor = stringResource(R.string.config_creditos_valor),
            aoClicar = aoAbrirSobre,
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

@Preview(widthDp = 390, heightDp = 844, showBackground = true)
@Composable
private fun ConfiguracoesPreview() {
    FitvaraTheme {
        ConfiguracoesScreen(navegacao = Navegacao())
    }
}
