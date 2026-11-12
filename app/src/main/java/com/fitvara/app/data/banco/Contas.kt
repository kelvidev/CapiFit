package com.fitvara.app.data.banco

import com.fitvara.app.data.entidades.Acesso
import com.fitvara.app.data.entidades.Usuario

const val EMAIL_CONVIDADO = "convidado"
const val NOME_CONVIDADO = "Convidado"
const val LEGENDA_CONVIDADO = "Explorando o CapiFIT"

suspend fun entrar(banco: BancoCapiFit, email: String, senha: String, hoje: String): Long? {
    val emailLimpo = email.trim()
    val usuario = banco.usuarioDao().buscarPorEmail(emailLimpo)

    if (usuario == null) {
        val nome = emailLimpo.substringBefore("@")
        val legenda = "No CapiFIT desde " + hoje.substring(0, 4)
        val novo = Usuario(email = emailLimpo, senha = senha, nome = nome, legenda = legenda)
        val id = banco.usuarioDao().inserir(novo)
        banco.acessoDao().registrar(Acesso(id, hoje))
        return id
    }

    if (usuario.senha.equals(senha)) {
        banco.acessoDao().registrar(Acesso(usuario.id, hoje))
        return usuario.id
    }

    return null
}

suspend fun entrarComoConvidado(banco: BancoCapiFit, hoje: String): Long {
    val convidado = banco.usuarioDao().buscarPorEmail(EMAIL_CONVIDADO)
    var id = 0L

    if (convidado == null) {
        val novo = Usuario(email = EMAIL_CONVIDADO, senha = "", nome = NOME_CONVIDADO, legenda = LEGENDA_CONVIDADO)
        id = banco.usuarioDao().inserir(novo)
    } else {
        id = convidado.id
    }

    banco.acessoDao().registrar(Acesso(id, hoje))
    return id
}

suspend fun excluirConta(banco: BancoCapiFit, usuarioId: Long) {
    banco.atividadeDao().excluirDoUsuario(usuarioId)
    banco.acessoDao().excluirDoUsuario(usuarioId)
    banco.usuarioDao().excluir(usuarioId)
}
