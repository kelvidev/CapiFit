package com.fitvara.app.ui.login

/** Minimo de caracteres aceito no campo de senha. */
const val SENHA_MINIMA = 6

private val FORMATO_EMAIL = Regex("^[^@\\s]+@[^@\\s]+\\.[^@\\s]{2,}$")

/**
 * Validacao de formato, so para dar retorno imediato no formulario. A regra de
 * verdade (conta existe, senha confere) vem com a autenticacao.
 */
fun emailValido(email: String): Boolean = FORMATO_EMAIL.matches(email.trim())

fun senhaValida(senha: String): Boolean = senha.length >= SENHA_MINIMA
