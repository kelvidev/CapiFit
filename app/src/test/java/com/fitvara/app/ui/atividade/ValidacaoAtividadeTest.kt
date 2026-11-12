package com.fitvara.app.ui.atividade

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidacaoAtividadeTest {
    @Test
    fun `filtro de digitos tira o resto e corta no maximo`() {
        assertEquals("280", filtrarDigitos("280", DIGITOS_DESNIVEL))
        assertEquals("280", filtrarDigitos("2a8-0 m", DIGITOS_DESNIVEL))
        assertEquals("12345", filtrarDigitos("1234567", DIGITOS_DESNIVEL))
        assertEquals("", filtrarDigitos("-,.", DIGITOS_DESNIVEL))

        assertEquals("", filtrarDigitos("٣٤", DIGITOS_DESNIVEL))
    }

    @Test
    fun `duracao guarda ate 4 digitos sem zero a esquerda`() {
        assertEquals("230", filtrarDuracao("230"))
        assertEquals("230", filtrarDuracao("0230"))
        assertEquals("", filtrarDuracao("0"))
        assertEquals("1234", filtrarDuracao("12345"))
        assertEquals("230", filtrarDuracao("2h 30min"))
    }

    @Test
    fun `distancia aceita uma virgula e ate tres casas`() {
        assertEquals("5,1", filtrarDistancia("5,1"))
        assertEquals("5,1", filtrarDistancia("5.1"))
        assertEquals("5,12", filtrarDistancia("5,1,2"))
        assertEquals("0,5", filtrarDistancia(",5"))
        assertEquals("1234,567", filtrarDistancia("12345,5678"))
        assertEquals("51", filtrarDistancia("5-1 km"))
    }

    @Test
    fun `duracao le os dois ultimos digitos como minutos`() {
        assertEquals(150, lerDuracao("230"))
        assertEquals(120, lerDuracao("200"))
        assertEquals(45, lerDuracao("45"))
        assertEquals(5, lerDuracao("5"))
        assertEquals(99 * 60 + 59, lerDuracao("9959"))
    }

    @Test
    fun `recusa duracao vazia, zerada ou com 60 minutos ou mais`() {
        assertNull(lerDuracao(""))
        assertNull(lerDuracao("0"))
        assertNull(lerDuracao("000"))
        assertNull(lerDuracao("60"))
        assertNull(lerDuracao("175"))
        assertNull(lerDuracao("2h"))
    }

    @Test
    fun `distancia precisa ser positiva`() {
        assertEquals(5.1, lerDistancia("5,1")!!, 0.0)
        assertEquals(5.1, lerDistancia("5.1")!!, 0.0)
        assertEquals(5.0, lerDistancia("5,")!!, 0.0)
        assertEquals(12.0, lerDistancia("12")!!, 0.0)
        assertNull(lerDistancia(""))
        assertNull(lerDistancia("0"))
        assertNull(lerDistancia("0,0"))
        assertNull(lerDistancia(","))
    }

    @Test
    fun `desnivel vazio vale zero`() {
        assertEquals(0, lerDesnivel(""))
        assertEquals(280, lerDesnivel("280"))
    }

    @Test
    fun `data do calendario vai e volta sem andar um dia`() {
        assertEquals(1790812800000L, isoParaMillisUtc("2026-10-01"))
        assertEquals("2026-10-01", millisUtcParaIso(1790812800000L))
        for (iso in listOf("1900-01-01", "2024-02-29", "2026-12-31")) {
            assertEquals(iso, millisUtcParaIso(isoParaMillisUtc(iso)))
        }
    }
}
