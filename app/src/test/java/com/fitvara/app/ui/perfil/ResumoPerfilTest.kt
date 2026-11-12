package com.fitvara.app.ui.perfil

import com.fitvara.app.data.entidades.Atividade
import com.fitvara.app.data.entidades.Dificuldade
import com.fitvara.app.data.entidades.TipoAtividade
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ResumoPerfilTest {

    private fun atividade(data: String, km: Double = 1.0) = Atividade(
        nome = "Teste",
        tipo = TipoAtividade.Trail,
        data = data,
        duracaoMin = 60,
        distanciaKm = km,
        desnivelM = 0,
        dificuldade = Dificuldade.Media,
    )

    @Test
    fun `recorde e a maior distancia`() {
        val lista = listOf(atividade("2026-10-01", 5.1), atividade("2026-10-02", 9.4), atividade("2026-10-03", 2.0))
        assertEquals(9.4, recordeKm(lista), 0.0001)
        assertEquals(0.0, recordeKm(emptyList()), 0.0001)
    }

    @Test
    fun `primeira atividade e a de data mais antiga`() {
        val lista = listOf(atividade("2026-10-20"), atividade("2026-09-05", 2.1), atividade("2026-10-01"))
        assertEquals("2026-09-05", primeiraAtividade(lista)?.data)
        assertNull(primeiraAtividade(emptyList()))
    }

    @Test
    fun `conta o mes com mais atividades`() {
        val lista = listOf(
            atividade("2026-09-01"),
            atividade("2026-10-01"),
            atividade("2026-10-05"),
            atividade("2026-10-20"),
        )
        assertEquals(3, maiorQuantidadeNoMes(lista))
        assertEquals(0, maiorQuantidadeNoMes(emptyList()))
    }
}
