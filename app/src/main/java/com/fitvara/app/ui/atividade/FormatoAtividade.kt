package com.fitvara.app.ui.atividade

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.util.Locale

fun dataBr(iso: String): String {
    val (ano, mes, dia) = iso.split("-")
    return "$dia/$mes/$ano"
}

fun duracaoTexto(minutos: Int): String {
    val horas = minutos / 60
    val resto = minutos % 60
    return when {
        horas == 0 -> "${resto}min"
        resto == 0 -> "${horas}h"
        else -> "${horas}h ${resto}min"
    }
}

fun duracaoDigitos(minutos: Int): String {
    val horas = minutos / 60
    val resto = minutos % 60

    return (horas * 100 + resto).toString()
}

fun distanciaTexto(km: Double): String {
    var texto = String.format(Locale.US, "%.3f", km)
    while (texto.endsWith("0") && !texto.endsWith(".0")) {
        texto = texto.dropLast(1)
    }
    return texto.replace('.', ',')
}

fun duracaoMascarada(digitos: String): String = when {
    digitos.isEmpty() -> ""
    digitos.length <= 2 -> "${digitos}min"
    else -> "${digitos.dropLast(2)}h ${digitos.takeLast(2)}min"
}

object MascaraDuracao : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digitos = text.text
        val mostrado = duracaoMascarada(digitos)
        val cursorNoFim = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = mostrado.length
            override fun transformedToOriginal(offset: Int): Int = digitos.length
        }
        return TransformedText(AnnotatedString(mostrado), cursorNoFim)
    }
}
