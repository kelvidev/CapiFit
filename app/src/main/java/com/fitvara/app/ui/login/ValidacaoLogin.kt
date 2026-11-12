package com.fitvara.app.ui.login

const val SENHA_MINIMA = 6

fun emailValido(email: String): Boolean {
    val texto = email.trim()
    if (texto.contains(" ")) {
        return false
    }

    val arroba = texto.indexOf("@")
    if (arroba <= 0 || arroba != texto.lastIndexOf("@")) {
        return false
    }

    val dominio = texto.substring(arroba + 1)
    val ponto = dominio.lastIndexOf(".")
    if (ponto <= 0) {
        return false
    }

    val final = dominio.substring(ponto + 1)
    return final.length >= 2
}

fun senhaValida(senha: String): Boolean {
    return senha.length >= SENHA_MINIMA
}
