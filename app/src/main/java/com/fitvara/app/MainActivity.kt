package com.fitvara.app

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.fitvara.app.data.banco.BancoCapiFit
import com.fitvara.app.data.banco.criarContasIniciais
import com.fitvara.app.data.banco.entrar
import com.fitvara.app.data.banco.entrarComoConvidado
import com.fitvara.app.data.banco.excluirConta
import com.fitvara.app.data.entidades.Atividade
import com.fitvara.app.ui.atividade.NovaAtividadeScreen
import com.fitvara.app.ui.atividade.hojeIso
import com.fitvara.app.ui.components.Navegacao
import com.fitvara.app.ui.configuracoes.ConfiguracoesScreen
import com.fitvara.app.ui.historico.DetalheAtividadeScreen
import com.fitvara.app.ui.historico.HistoricoScreen
import com.fitvara.app.ui.inicio.InicioScreen
import com.fitvara.app.ui.login.LoginScreen
import com.fitvara.app.ui.perfil.PerfilScreen
import com.fitvara.app.ui.perfil.conquistasFeitas
import com.fitvara.app.ui.sobre.SobreScreen
import com.fitvara.app.ui.theme.FitvaraTheme
import kotlinx.coroutines.launch

private enum class Tela { Login, Inicio, NovaAtividade, Historico, Detalhe, Perfil, Configuracoes, Sobre }
private const val LIMITE_FAVORITAS = 3

class MainActivity : ComponentActivity() {

    private val banco by lazy { BancoCapiFit.abrir(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.BLACK),
            navigationBarStyle = SystemBarStyle.light(Color.TRANSPARENT, Color.BLACK),
        )
        super.onCreate(savedInstanceState)
        setContent {
            FitvaraTheme {
                CapiFit(banco)
            }
        }
    }

    override fun onDestroy() {
        banco.close()
        super.onDestroy()
    }
}

@Composable
private fun CapiFit(banco: BancoCapiFit) {
    var tela by rememberSaveable { mutableStateOf(Tela.Login) }
    var usuarioId by rememberSaveable { mutableLongStateOf(0L) }
    var editandoId by rememberSaveable { mutableStateOf<Long?>(null) }
    var detalheId by rememberSaveable { mutableLongStateOf(0L) }
    val escopo = rememberCoroutineScope()
    val contexto = LocalContext.current

    LaunchedEffect(Unit) {
        criarContasIniciais(banco)
    }

    val usuarioBanco = remember(usuarioId) { banco.usuarioDao().buscar(usuarioId) }
    val atividadesBanco = remember(usuarioId) { banco.atividadeDao().listar(usuarioId) }
    val diasBanco = remember(usuarioId) { banco.acessoDao().listarDias(usuarioId) }

    val usuario by usuarioBanco.collectAsState(initial = null)
    val atividades by atividadesBanco.collectAsState(initial = emptyList())
    val diasDeAcesso by diasBanco.collectAsState(initial = emptyList())

    val nome = usuario?.nome ?: ""
    val legenda = usuario?.legenda ?: ""
    var inicial = "A"
    if (nome.isNotEmpty()) {
        inicial = nome.substring(0, 1).uppercase()
    }

    fun ir(destino: Tela) {
        editandoId = null
        tela = destino
    }

    fun sair() {
        usuarioId = 0L
        ir(Tela.Login)
    }

    fun avisar(mensagem: Int, vararg valores: Any) {
        Toast.makeText(contexto, contexto.getString(mensagem, *valores), Toast.LENGTH_SHORT).show()
    }

    fun voltar() {
        when (tela) {
            Tela.Detalhe -> ir(Tela.Historico)
            Tela.Sobre -> ir(Tela.Configuracoes)
            Tela.NovaAtividade -> {
                if (editandoId != null) {
                    ir(Tela.Historico)
                } else {
                    ir(Tela.Inicio)
                }
            }
            else -> ir(Tela.Inicio)
        }
    }

    fun abrirDetalhe(atividade: Atividade) {
        detalheId = atividade.id
        ir(Tela.Detalhe)
    }

    fun editar(atividade: Atividade) {
        ir(Tela.NovaAtividade)
        editandoId = atividade.id
    }

    fun favoritar(atividade: Atividade) {
        escopo.launch {
            val total = banco.atividadeDao().contarFavoritas(usuarioId)

            if(atividade.favorita){
                banco.atividadeDao().atualizar(atividade.copy(favorita = false))
                avisar(R.string.atividade_desfavoritada)
                return@launch
            }

            if (total < LIMITE_FAVORITAS) {
                banco.atividadeDao().atualizar(atividade.copy(favorita = true))
                avisar(R.string.atividade_favoritada, total + 1, LIMITE_FAVORITAS)
                return@launch
            }

            avisar(R.string.favoritas_limite, LIMITE_FAVORITAS)
        }
    }

    fun excluir(atividade: Atividade) {
        escopo.launch {
            banco.atividadeDao().excluir(atividade.id)
            avisar(R.string.atividade_excluida)
        }
    }

    BackHandler(enabled = tela != Tela.Login && tela != Tela.Inicio) {
        voltar()
    }

    val navegacao = Navegacao(
        aoInicio = { ir(Tela.Inicio) },
        aoHistorico = { ir(Tela.Historico) },
        aoLogar = { ir(Tela.NovaAtividade) },
        aoPerfil = { ir(Tela.Perfil) },
        aoConfiguracoes = { ir(Tela.Configuracoes) },
        inicial = inicial,
    )

    when (tela) {
        Tela.Login -> LoginScreen(
            onEntrar = { email, senha ->
                val id = entrar(banco, email, senha, hojeIso())
                if (id != null) {
                    usuarioId = id
                    ir(Tela.Inicio)
                }
                id != null
            },
            onConvidado = {
                escopo.launch {
                    usuarioId = entrarComoConvidado(banco, hojeIso())
                    ir(Tela.Inicio)
                }
            },
            modifier = Modifier.fillMaxSize(),
        )

        Tela.Inicio -> InicioScreen(
            nome = nome,
            atividades = atividades,
            diasDeAcesso = diasDeAcesso,
            navegacao = navegacao,
            modifier = Modifier.fillMaxSize(),
        )

        Tela.NovaAtividade -> {
            var inicial: Atividade? = null
            var eraFavorita = false
            for (atividade in atividades) {
                if (atividade.id == editandoId) {
                    inicial = atividade
                    eraFavorita = atividade.favorita
                }
            }

            key(editandoId) {
                NovaAtividadeScreen(
                    navegacao = navegacao,
                    modifier = Modifier.fillMaxSize(),
                    inicial = inicial,
                    aoSalvar = { atividade ->
                        escopo.launch {
                            val comDono = atividade.copy(usuarioId = usuarioId, favorita = eraFavorita)
                            val antes = conquistasFeitas(atividades)
                            val depois: List<Atividade>

                            if (comDono.id == 0L) {
                                banco.atividadeDao().inserir(comDono)
                                avisar(R.string.atividade_salva)
                                depois = atividades + comDono
                            } else {
                                banco.atividadeDao().atualizar(comDono)
                                avisar(R.string.atividade_atualizada)
                                depois = atividades.map{ if(it.id == comDono.id) comDono else it }
                            }

                            if(conquistasFeitas(depois) > antes){
                                avisar(R.string.conquista_desbloqueada)
                            }

                            ir(Tela.Historico)
                        }
                    },
                )
            }
        }

        Tela.Historico -> HistoricoScreen(
            atividades = atividades,
            navegacao = navegacao,
            modifier = Modifier.fillMaxSize(),
            aoAbrir = { atividade -> abrirDetalhe(atividade) },
            aoFavoritar = { atividade -> favoritar(atividade) },
            aoEditar = { atividade -> editar(atividade) },
            aoExcluir = { atividade -> excluir(atividade) },
        )

        Tela.Detalhe -> {
            var aberta: Atividade? = null
            for (atividade in atividades) {
                if (atividade.id == detalheId) {
                    aberta = atividade
                }
            }

            if (aberta != null) {
                val atividade = aberta
                DetalheAtividadeScreen(
                    atividade = atividade,
                    navegacao = navegacao,
                    modifier = Modifier.fillMaxSize(),
                    aoFavoritar = { favoritar(atividade) },
                    aoEditar = { editar(atividade) },
                    aoExcluir = {
                        excluir(atividade)
                        ir(Tela.Historico)
                    },
                )
            }
        }

        Tela.Perfil -> PerfilScreen(
            nome = nome,
            legenda = legenda,
            atividades = atividades,
            navegacao = navegacao,
            modifier = Modifier.fillMaxSize(),
        )

        Tela.Configuracoes -> ConfiguracoesScreen(
            navegacao = navegacao,
            modifier = Modifier.fillMaxSize(),
            aoAbrirSobre = { ir(Tela.Sobre) },
            aoSair = { sair() },
            aoSalvarPerfil = { novoNome, novaLegenda ->
                val atual = usuario
                if (atual != null) {
                    escopo.launch {
                        banco.usuarioDao().atualizar(atual.copy(nome = novoNome, legenda = novaLegenda))
                    }
                }
            },
            aoExcluirConta = {
                escopo.launch {
                    excluirConta(banco, usuarioId)
                    sair()
                }
            },
            nome = nome,
            legenda = legenda,
        )

        Tela.Sobre -> SobreScreen(
            navegacao = navegacao,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
