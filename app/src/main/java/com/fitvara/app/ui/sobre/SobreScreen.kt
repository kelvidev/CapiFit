package com.fitvara.app.ui.sobre

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.fitvara.app.BuildConfig
import com.fitvara.app.R
import com.fitvara.app.ui.components.EsqueletoTela
import com.fitvara.app.ui.components.ItemBarra
import com.fitvara.app.ui.components.Navegacao
import com.fitvara.app.ui.theme.Borda
import com.fitvara.app.ui.theme.Branco
import com.fitvara.app.ui.theme.FitvaraTheme
import com.fitvara.app.ui.theme.Sol
import com.fitvara.app.ui.theme.Tinta
import com.fitvara.app.ui.theme.TintaSuave

private val CantoCartao = RoundedCornerShape(16.dp)

private val Equipe = listOf(
    "Christian Andrade de Lima",
    "Kelvi Moacir de Sousa Carvalho",
    "Ryann Gustavo Valim Luiz",
)

@Composable
fun SobreScreen(
    navegacao: Navegacao,
    modifier: Modifier = Modifier,
) {
    EsqueletoTela(
        navegacao = navegacao,
        item = ItemBarra.Nenhum,
        modifier = modifier,
        titulo = stringResource(R.string.sobre_titulo),
        subtitulo = stringResource(R.string.sobre_subtitulo),
    ) {
        Spacer(Modifier.height(18.dp))

        Cartao {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(Sol),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.capivara_avatar),
                        contentDescription = null,
                        tint = Tinta,
                        modifier = Modifier.size(40.dp),
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    Text(
                        text = stringResource(R.string.app_marca),
                        style = MaterialTheme.typography.titleLarge,
                        color = Tinta,
                    )
                    Text(
                        text = stringResource(R.string.app_tagline),
                        style = MaterialTheme.typography.bodySmall,
                        color = TintaSuave,
                    )
                }
            }
            Spacer(Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.sobre_objetivo),
                style = MaterialTheme.typography.bodyMedium,
                color = Tinta,
            )
        }

        Spacer(Modifier.height(10.dp))

        Cartao {
            Item(stringResource(R.string.sobre_versao), BuildConfig.VERSION_NAME)
            Spacer(Modifier.height(12.dp))
            Item(stringResource(R.string.sobre_disciplina), stringResource(R.string.sobre_disciplina_nome))
        }

        Spacer(Modifier.height(10.dp))

        Cartao {
            Rotulo(stringResource(R.string.sobre_equipe))
            for (nome in Equipe) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = nome,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = Tinta,
                )
            }
        }

        Spacer(Modifier.height(10.dp))

        Cartao {
            Rotulo(stringResource(R.string.sobre_creditos))
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.sobre_creditos_icones),
                style = MaterialTheme.typography.bodyMedium,
                color = Tinta,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(R.string.sobre_creditos_capivara),
                style = MaterialTheme.typography.bodyMedium,
                color = Tinta,
            )
        }
    }
}

@Composable
private fun Cartao(conteudo: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Branco.copy(alpha = 0.68f), CantoCartao)
            .border(1.dp, Borda, CantoCartao)
            .padding(horizontal = 16.dp, vertical = 14.dp),
    ) {
        conteudo()
    }
}

@Composable
private fun Rotulo(texto: String) {
    Text(
        text = texto,
        style = MaterialTheme.typography.bodySmall,
        color = TintaSuave,
    )
}

@Composable
private fun Item(rotulo: String, valor: String) {
    Rotulo(rotulo)
    Spacer(Modifier.height(2.dp))
    Text(
        text = valor,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
        color = Tinta,
    )
}

@Preview(widthDp = 390, heightDp = 844, showBackground = true)
@Composable
private fun SobrePreview() {
    FitvaraTheme {
        SobreScreen(navegacao = Navegacao())
    }
}
