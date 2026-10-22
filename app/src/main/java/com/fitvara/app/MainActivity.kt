package com.fitvara.app

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.fitvara.app.ui.atividade.NovaAtividadeScreen
import com.fitvara.app.ui.components.Navegacao
import com.fitvara.app.ui.configuracoes.ConfiguracoesScreen
import com.fitvara.app.ui.historico.HistoricoScreen
import com.fitvara.app.ui.inicio.InicioScreen
import com.fitvara.app.ui.login.LoginScreen
import com.fitvara.app.ui.perfil.PerfilScreen
import com.fitvara.app.ui.theme.FitvaraTheme

/** As telas do app. A navegacao e so troca de estado — ainda nao vale uma lib. */
private enum class Tela { Login, Inicio, NovaAtividade, Historico, Perfil, Configuracoes }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        // O app e claro em qualquer aparelho, entao os icones das barras ficam escuros.
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.BLACK),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.BLACK),
        )
        super.onCreate(savedInstanceState)
        setContent {
            FitvaraTheme {
                CapiFit()
            }
        }
    }
}

@Composable
private fun CapiFit() {
    var tela by rememberSaveable { mutableStateOf(Tela.Login) }

    val navegacao = remember {
        Navegacao(
            aoInicio = { tela = Tela.Inicio },
            aoHistorico = { tela = Tela.Historico },
            aoLogar = { tela = Tela.NovaAtividade },
            aoPerfil = { tela = Tela.Perfil },
            aoConfiguracoes = { tela = Tela.Configuracoes },
        )
    }

    when (tela) {
        Tela.Login -> LoginScreen(
            onEntrar = { tela = Tela.Perfil },
            onConvidado = { tela = Tela.Perfil },
            modifier = Modifier.fillMaxSize(),
        )

        Tela.Perfil -> PerfilScreen(
            navegacao = navegacao,
            modifier = Modifier.fillMaxSize(),
            aoExcluirConta = { tela = Tela.Login },
        )

        Tela.Inicio -> InicioScreen(
            navegacao = navegacao,
            modifier = Modifier.fillMaxSize(),
        )

        Tela.NovaAtividade -> NovaAtividadeScreen(
            navegacao = navegacao,
            modifier = Modifier.fillMaxSize(),
            aoSalvar = { tela = Tela.Historico },
        )

        Tela.Historico -> HistoricoScreen(
            navegacao = navegacao,
            modifier = Modifier.fillMaxSize(),
            aoEditar = { tela = Tela.NovaAtividade },
        )

        Tela.Configuracoes -> ConfiguracoesScreen(
            navegacao = navegacao,
            modifier = Modifier.fillMaxSize(),
            aoSair = { tela = Tela.Login },
        )
    }
}
