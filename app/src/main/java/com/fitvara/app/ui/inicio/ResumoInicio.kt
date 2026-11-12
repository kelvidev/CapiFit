package com.fitvara.app.ui.inicio

import com.fitvara.app.data.entidades.Atividade
import com.fitvara.app.data.entidades.TipoAtividade
import com.fitvara.app.ui.atividade.isoParaMillisUtc
import com.fitvara.app.ui.atividade.millisUtcParaIso

const val SEMANAS = 12

private const val UM_DIA = 24 * 60 * 60 * 1000L

private val NomesDosMeses = listOf("JAN", "FEV", "MAR", "ABR", "MAI", "JUN", "JUL", "AGO", "SET", "OUT", "NOV", "DEZ")

data class RotuloMes(val nome: String, val semana: Int)

fun somarDias(data: String, dias: Int): String =
    millisUtcParaIso(isoParaMillisUtc(data) + dias * UM_DIA)

fun diasEntre(inicio: String, fim: String): Int =
    ((isoParaMillisUtc(fim) - isoParaMillisUtc(inicio)) / UM_DIA).toInt()

fun filtrarPorChip(atividades: List<Atividade>, chip: Int): List<Atividade> {
    if (chip == 0) return atividades
    val tipo = TipoAtividade.entries[chip - 1]
    val doTipo = mutableListOf<Atividade>()
    for (atividade in atividades) {
        if (atividade.tipo == tipo) {
            doTipo.add(atividade)
        }
    }
    return doTipo
}

fun inicioDoPeriodo(hoje: String): String = somarDias(hoje, -(SEMANAS * 7 - 1))

fun atividadesDoPeriodo(atividades: List<Atividade>, hoje: String): List<Atividade> {
    val inicio = inicioDoPeriodo(hoje)
    val doPeriodo = mutableListOf<Atividade>()
    for (atividade in atividades) {
        if (atividade.data >= inicio && atividade.data <= hoje) {
            doPeriodo.add(atividade)
        }
    }
    return doPeriodo
}

fun totalMinutos(atividades: List<Atividade>): Int {
    var total = 0
    for (atividade in atividades) {
        total += atividade.duracaoMin
    }
    return total
}

fun totalKm(atividades: List<Atividade>): Double {
    var total = 0.0
    for (atividade in atividades) {
        total += atividade.distanciaKm
    }
    return total
}

fun kmPorSemana(atividades: List<Atividade>, hoje: String): List<Double> {
    val inicio = inicioDoPeriodo(hoje)
    val km = MutableList(SEMANAS) { 0.0 }
    for (atividade in atividades) {
        val dias = diasEntre(inicio, atividade.data)

        if (dias < 0 || dias >= SEMANAS * 7) continue
        val semana = dias / 7
        km[semana] = km[semana] + atividade.distanciaKm
    }
    return km
}

fun desnivelNoMes(atividades: List<Atividade>, hoje: String): Int {
    val mesAtual = hoje.substring(0, 7)
    var total = 0
    for (atividade in atividades) {
        if (atividade.data.startsWith(mesAtual)) {
            total += atividade.desnivelM
        }
    }
    return total
}

fun sequenciaDeDias(dias: List<String>, hoje: String): Int {
    val diasComTreino = mutableSetOf<String>()
    for (dia in dias) {
        diasComTreino.add(dia)
    }

    var dia = hoje
    if (dia !in diasComTreino) {
        dia = somarDias(hoje, -1)
    }

    var seguidos = 0
    while (dia in diasComTreino) {
        seguidos++
        dia = somarDias(dia, -1)
    }
    return seguidos
}

fun mesesDoEixo(hoje: String): List<RotuloMes> {
    val inicio = inicioDoPeriodo(hoje)
    val rotulos = mutableListOf<RotuloMes>()
    var mesAnterior = ""

    for (semana in 0 until SEMANAS) {
        val mes = somarDias(inicio, semana * 7).substring(5, 7)
        if (mes == mesAnterior) continue
        mesAnterior = mes

        val rotulo = RotuloMes(NomesDosMeses[mes.toInt() - 1], semana)
        if (rotulos.isNotEmpty() && semana - rotulos.last().semana < 2) {
            rotulos[rotulos.size - 1] = rotulo
        } else {
            rotulos.add(rotulo)
        }
    }
    return rotulos
}
