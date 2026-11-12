package com.fitvara.app.ui.inicio

import com.fitvara.app.data.entidades.Atividade
import com.fitvara.app.data.entidades.Dificuldade
import com.fitvara.app.data.entidades.TipoAtividade
import org.junit.Assert.assertEquals
import org.junit.Test

class ResumoInicioTest {
    private val hoje = "2026-10-26"

    private fun atividade(
        data: String,
        km: Double = 1.0,
        tipo: TipoAtividade = TipoAtividade.Trail,
        desnivel: Int = 0,
        minutos: Int = 60,
    ) = Atividade(
        nome = "Teste",
        tipo = tipo,
        data = data,
        duracaoMin = minutos,
        distanciaKm = km,
        desnivelM = desnivel,
        dificuldade = Dificuldade.Media,
    )

    @Test
    fun `soma e conta dias entre datas, virando mes e ano`() {
        assertEquals("2026-10-25", somarDias(hoje, -1))
        assertEquals("2026-11-01", somarDias("2026-10-31", 1))
        assertEquals("2027-01-01", somarDias("2026-12-31", 1))
        assertEquals(83, diasEntre("2026-08-04", hoje))
    }

    @Test
    fun `periodo tem 12 semanas terminando hoje`() {
        assertEquals("2026-08-04", inicioDoPeriodo(hoje))
        val lista = listOf(atividade("2026-08-03"), atividade("2026-08-04"), atividade(hoje))
        assertEquals(2, atividadesDoPeriodo(lista, hoje).size)
    }

    @Test
    fun `chip filtra pelo tipo e Todos devolve tudo`() {
        val lista = listOf(
            atividade(hoje, tipo = TipoAtividade.Run),
            atividade(hoje, tipo = TipoAtividade.Trail),
            atividade(hoje, tipo = TipoAtividade.Trail),
        )
        assertEquals(3, filtrarPorChip(lista, 0).size)
        assertEquals(1, filtrarPorChip(lista, 1).size)
        assertEquals(2, filtrarPorChip(lista, 2).size)
        assertEquals(0, filtrarPorChip(lista, 3).size)
    }

    @Test
    fun `totais somam tempo e distancia`() {
        val lista = listOf(atividade(hoje, km = 5.1, minutos = 150), atividade(hoje, km = 3.4, minutos = 75))
        assertEquals(225, totalMinutos(lista))
        assertEquals(8.5, totalKm(lista), 0.0001)
    }

    @Test
    fun `km por semana coloca cada atividade na sua semana`() {
        val lista = listOf(
            atividade("2026-08-04", km = 2.0),
            atividade("2026-08-10", km = 1.0),
            atividade("2026-08-11", km = 4.0),
            atividade(hoje, km = 5.0),
            atividade("2026-08-03", km = 9.0),
        )
        val km = kmPorSemana(lista, hoje)
        assertEquals(12, km.size)
        assertEquals(3.0, km[0], 0.0001)
        assertEquals(4.0, km[1], 0.0001)
        assertEquals(5.0, km[11], 0.0001)
        assertEquals(12.0, km.sum(), 0.0001)
    }

    @Test
    fun `desnivel soma so o mes atual`() {
        val lista = listOf(
            atividade("2026-10-01", desnivel = 200),
            atividade(hoje, desnivel = 120),
            atividade("2026-09-30", desnivel = 999),
        )
        assertEquals(320, desnivelNoMes(lista, hoje))
    }

    @Test
    fun `sequencia conta dias seguidos ate hoje ou ate ontem`() {
        val comHoje = listOf(hoje, "2026-10-25", "2026-10-24", "2026-10-22")
        assertEquals(3, sequenciaDeDias(comHoje, hoje))

        val semHoje = listOf("2026-10-25", "2026-10-24")
        assertEquals(2, sequenciaDeDias(semHoje, hoje))

        val parada = listOf("2026-10-23")
        assertEquals(0, sequenciaDeDias(parada, hoje))
        assertEquals(0, sequenciaDeDias(emptyList(), hoje))
    }

    @Test
    fun `eixo mostra o mes onde ele comeca`() {
        assertEquals(
            listOf(RotuloMes("AGO", 0), RotuloMes("SET", 4), RotuloMes("OUT", 9)),
            mesesDoEixo(hoje),
        )
    }

    @Test
    fun `rotulo colado no anterior toma o lugar dele`() {
        assertEquals("2026-07-27", inicioDoPeriodo("2026-10-18"))
        assertEquals(RotuloMes("AGO", 1), mesesDoEixo("2026-10-18").first())
    }
}
