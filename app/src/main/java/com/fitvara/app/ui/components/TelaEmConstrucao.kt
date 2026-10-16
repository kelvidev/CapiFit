package com.fitvara.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fitvara.app.R
import com.fitvara.app.ui.theme.Borda
import com.fitvara.app.ui.theme.Branco

/**
 * Lugar reservado para as telas que ainda nao foram feitas. Mantem cabecalho e barra
 * inferior para a navegacao continuar funcionando enquanto cada tela chega.
 */
@Composable
fun TelaEmConstrucao(
    titulo: String,
    arquivoDoDesenho: String,
    navegacao: Navegacao,
    item: ItemBarra,
    modifier: Modifier = Modifier,
) {
    EsqueletoTela(
        navegacao = navegacao,
        item = item,
        modifier = modifier,
        titulo = titulo,
    ) {
        Spacer(Modifier.height(40.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Branco.copy(alpha = 0.62f), RoundedCornerShape(20.dp))
                .border(1.dp, Borda, RoundedCornerShape(20.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.em_construcao_titulo),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
            )
            Text(
                text = stringResource(R.string.em_construcao_detalhe, arquivoDoDesenho),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        }
    }
}
