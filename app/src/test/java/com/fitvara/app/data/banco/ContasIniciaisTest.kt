package com.fitvara.app.data.banco

import com.fitvara.app.ui.login.emailValido
import com.fitvara.app.ui.login.senhaValida
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ContasIniciaisTest {

    @Test
    fun `tres contas do time`() {
        assertEquals(3, CONTAS_INICIAIS.size)
        assertEquals("Christian", CONTAS_INICIAIS[0].nome)
        assertEquals("Kelvi", CONTAS_INICIAIS[1].nome)
        assertEquals("Ryann", CONTAS_INICIAIS[2].nome)
    }

    @Test
    fun `contas passam na validacao do login`() {
        for (conta in CONTAS_INICIAIS) {
            assertTrue(conta.email, emailValido(conta.email))
            assertTrue(conta.senha, senhaValida(conta.senha))
        }
    }
}
