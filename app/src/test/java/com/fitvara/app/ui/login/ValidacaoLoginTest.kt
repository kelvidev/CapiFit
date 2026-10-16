package com.fitvara.app.ui.login

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidacaoLoginTest {

    @Test
    fun `aceita email comum`() {
        assertTrue(emailValido("ana@exemplo.com"))
        assertTrue(emailValido("ana.souza+trilha@exemplo.com.br"))
    }

    @Test
    fun `ignora espaco em volta do email`() {
        assertTrue(emailValido("  ana@exemplo.com  "))
    }

    @Test
    fun `recusa email sem arroba, sem dominio ou vazio`() {
        assertFalse(emailValido(""))
        assertFalse(emailValido("ana"))
        assertFalse(emailValido("ana@exemplo"))
        assertFalse(emailValido("ana@exemplo.c"))
        assertFalse(emailValido("ana souza@exemplo.com"))
    }

    @Test
    fun `senha precisa do minimo de caracteres`() {
        assertFalse(senhaValida("12345"))
        assertTrue(senhaValida("123456"))
        assertTrue(senhaValida("trilha-da-serra"))
    }
}
