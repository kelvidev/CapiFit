package com.fitvara.app.ui.atividade

import androidx.compose.ui.text.AnnotatedString
import org.junit.Assert.assertEquals
import org.junit.Test

class FormatoAtividadeTest {
    @Test
    fun `data volta para dd-MM-aaaa`() {
        assertEquals("01/10/2026", dataBr("2026-10-01"))
    }

    @Test
    fun `duracao no formato do desenho`() {
        assertEquals("2h 30min", duracaoTexto(150))
        assertEquals("2h", duracaoTexto(120))
        assertEquals("45min", duracaoTexto(45))
        assertEquals("3h 5min", duracaoTexto(185))
    }

    @Test
    fun `distancia com virgula e ao menos uma casa`() {
        assertEquals("5,1", distanciaTexto(5.1))
        assertEquals("12,0", distanciaTexto(12.0))
        assertEquals("8,25", distanciaTexto(8.25))
        assertEquals("0,125", distanciaTexto(0.125))
        assertEquals("100,0", distanciaTexto(100.0))
    }

    @Test
    fun `mascara mostra os digitos como horas e minutos`() {
        assertEquals("", duracaoMascarada(""))
        assertEquals("2min", duracaoMascarada("2"))
        assertEquals("23min", duracaoMascarada("23"))
        assertEquals("2h 30min", duracaoMascarada("230"))
        assertEquals("12h 05min", duracaoMascarada("1205"))
    }

    @Test
    fun `mascara mostra o texto formatado com o cursor sempre no fim`() {
        val resultado = MascaraDuracao.filter(AnnotatedString("230"))
        assertEquals("2h 30min", resultado.text.text)

        assertEquals(8, resultado.offsetMapping.originalToTransformed(1))
        assertEquals(3, resultado.offsetMapping.transformedToOriginal(2))
    }

    @Test
    fun `o que vem do banco volta igual para o formulario`() {
        for (minutos in listOf(1, 45, 59, 60, 61, 150, 185, 600, 99 * 60 + 59)) {
            assertEquals(minutos, lerDuracao(filtrarDuracao(duracaoDigitos(minutos))))
        }
        for (km in listOf(5.1, 12.0, 8.25, 0.125, 9999.999)) {
            assertEquals(km, lerDistancia(filtrarDistancia(distanciaTexto(km)))!!, 0.0)
        }
    }
}
