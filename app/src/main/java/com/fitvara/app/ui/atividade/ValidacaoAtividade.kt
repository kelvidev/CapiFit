package com.fitvara.app.ui.atividade

import java.util.Calendar
import java.util.TimeZone

const val DIGITOS_DURACAO = 4

const val DIGITOS_DESNIVEL = 5

fun filtrarDigitos(texto: String, maximo: Int): String {
    var saida = ""
    for (c in texto) {
        if (c in '0'..'9' && saida.length < maximo) {
            saida += c
        }
    }
    return saida
}

fun filtrarDuracao(texto: String): String =
    filtrarDigitos(texto, DIGITOS_DURACAO).trimStart('0')

fun filtrarDistancia(texto: String): String {
    var saida = ""
    var temVirgula = false
    var inteiros = 0
    var casas = 0
    for (c in texto) {
        val ehDigito = c in '0'..'9'
        val ehVirgula = c == ',' || c == '.'
        if (ehDigito && !temVirgula && inteiros < 4) {
            saida += c
            inteiros++
        } else if (ehDigito && temVirgula && casas < 3) {
            saida += c
            casas++
        } else if (ehVirgula && !temVirgula) {
            if (inteiros == 0) {
                saida += "0"
            }
            saida += ","
            temVirgula = true
        }
    }
    return saida
}

fun lerDuracao(digitos: String): Int? {
    val numero = digitos.toIntOrNull() ?: return null
    val horas = numero / 100
    val minutos = numero % 100
    if (minutos >= 60) return null
    val total = horas * 60 + minutos
    if (total <= 0) return null
    return total
}

fun lerDistancia(texto: String): Double? {
    val numero = texto.replace(',', '.').toDoubleOrNull() ?: return null
    if (numero <= 0) return null
    return numero
}

fun lerDesnivel(texto: String): Int = texto.toIntOrNull() ?: 0

fun hojeIso(): String {
    val agora = Calendar.getInstance()
    return iso(agora.get(Calendar.YEAR), agora.get(Calendar.MONTH) + 1, agora.get(Calendar.DAY_OF_MONTH))
}

fun isoParaMillisUtc(iso: String): Long {
    val partes = iso.split("-")
    val ano = partes[0].toInt()
    val mes = partes[1].toInt()
    val dia = partes[2].toInt()
    val calendario = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendario.clear()
    calendario.set(ano, mes - 1, dia)
    return calendario.timeInMillis
}

fun millisUtcParaIso(millis: Long): String {
    val calendario = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    calendario.timeInMillis = millis
    val ano = calendario.get(Calendar.YEAR)
    val mes = calendario.get(Calendar.MONTH) + 1
    val dia = calendario.get(Calendar.DAY_OF_MONTH)
    return iso(ano, mes, dia)
}

private fun iso(ano: Int, mes: Int, dia: Int): String =
    "$ano-${mes.toString().padStart(2, '0')}-${dia.toString().padStart(2, '0')}"
