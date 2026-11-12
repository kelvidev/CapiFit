package com.fitvara.app.ui.perfil

import com.fitvara.app.data.entidades.Atividade

fun recordeKm(atividades: List<Atividade>): Double {
    var maior = 0.0
    for (atividade in atividades) {
        if (atividade.distanciaKm > maior) {
            maior = atividade.distanciaKm
        }
    }
    return maior
}

fun primeiraAtividade(atividades: List<Atividade>): Atividade? {
    var primeira: Atividade? = null
    for (atividade in atividades) {
        if (primeira == null || atividade.data < primeira.data) {
            primeira = atividade
        }
    }
    return primeira
}

fun maiorQuantidadeNoMes(atividades: List<Atividade>): Int {
    val porMes = mutableMapOf<String, Int>()
    for (atividade in atividades) {
        val mes = atividade.data.substring(0, 7)
        val quantidade = porMes[mes] ?: 0
        porMes[mes] = quantidade + 1
    }

    var maior = 0
    for (quantidade in porMes.values) {
        if (quantidade > maior) {
            maior = quantidade
        }
    }
    return maior
}
