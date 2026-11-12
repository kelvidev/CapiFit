package com.fitvara.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitvara.app.R
import com.fitvara.app.ui.theme.Borda
import com.fitvara.app.ui.theme.Creme
import com.fitvara.app.ui.theme.Duna
import com.fitvara.app.ui.theme.Folha
import com.fitvara.app.ui.theme.FolhaEscura
import com.fitvara.app.ui.theme.Sol
import com.fitvara.app.ui.theme.TintaSuave

private val Margem = 20.dp

enum class ItemBarra { Nenhum, Historico, Logar, Perfil }

data class Navegacao(
    val aoInicio: () -> Unit = {},
    val aoHistorico: () -> Unit = {},
    val aoLogar: () -> Unit = {},
    val aoPerfil: () -> Unit = {},
    val aoConfiguracoes: () -> Unit = {},
    val inicial: String = "A",
)

@Composable
fun EsqueletoTela(
    navegacao: Navegacao,
    item: ItemBarra,
    modifier: Modifier = Modifier,
    titulo: String? = null,
    subtitulo: String? = null,
    conteudo: @Composable ColumnScope.() -> Unit,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        bottomBar = { BarraNavegacao(navegacao, item) },
    ) { espacamento ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(espacamento)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Margem),
        ) {
            Spacer(Modifier.height(10.dp))
            CabecalhoTopo(navegacao, titulo, subtitulo)
            conteudo()
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun CabecalhoTopo(
    navegacao: Navegacao,
    titulo: String? = null,
    subtitulo: String? = null,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            if (titulo != null) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp, lineHeight = 22.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
            if (subtitulo != null) {
                Text(
                    text = subtitulo,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        BotaoCabecalho(
            cor = Duna,
            descricao = stringResource(R.string.nav_inicio),
            aoClicar = navegacao.aoInicio,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_casinha),
                contentDescription = null,
                tint = FolhaEscura,
                modifier = Modifier.size(18.dp),
            )
        }

        Spacer(Modifier.width(8.dp))

        BotaoCabecalho(
            cor = Sol,
            descricao = stringResource(R.string.nav_configuracoes),
            aoClicar = navegacao.aoConfiguracoes,
        ) {
            Text(
                text = navegacao.inicial,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground,
            )
        }
    }
}

@Composable
private fun BotaoCabecalho(
    cor: Color,
    descricao: String,
    aoClicar: () -> Unit,
    conteudo: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(cor)
            .clickable(onClick = aoClicar)
            .semantics { contentDescription = descricao },
        contentAlignment = Alignment.Center,
        content = { conteudo() },
    )
}

@Composable
fun BarraNavegacao(
    navegacao: Navegacao,
    item: ItemBarra,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .height(94.dp),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(64.dp)
                .background(Creme, RoundedCornerShape(24.dp))
                .border(1.dp, Borda, RoundedCornerShape(24.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ItemNavegacao(
                rotulo = stringResource(R.string.nav_historico),
                selecionado = item == ItemBarra.Historico,
                aoClicar = navegacao.aoHistorico,
                modifier = Modifier.weight(1f),
                icone = R.drawable.ic_historico,
            )

            Spacer(Modifier.weight(1f))

            ItemNavegacao(
                rotulo = stringResource(R.string.nav_perfil),
                selecionado = item == ItemBarra.Perfil,
                aoClicar = navegacao.aoPerfil,
                modifier = Modifier.weight(1f),
                icone = R.drawable.ic_pessoa,
            )
        }

        Column(
            modifier = Modifier.align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(Folha)
                    .clickable(onClick = navegacao.aoLogar),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_mais),
                    contentDescription = null,
                    tint = Creme,
                    modifier = Modifier.size(22.dp),
                )
            }

            Spacer(Modifier.height(6.dp))

            Text(
                text = stringResource(R.string.nav_logar),
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = FolhaEscura,
            )
        }
    }
}

@Composable
private fun ItemNavegacao(
    rotulo: String,
    selecionado: Boolean,
    aoClicar: () -> Unit,
    icone: Int,
    modifier: Modifier = Modifier,
) {
    val cor = if (selecionado) FolhaEscura else TintaSuave

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = aoClicar)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Icon(
            painter = painterResource(icone),
            contentDescription = null,
            tint = cor,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = rotulo,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = if (selecionado) FontWeight.Bold else FontWeight.SemiBold,
            ),
            color = cor,
        )
    }
}
