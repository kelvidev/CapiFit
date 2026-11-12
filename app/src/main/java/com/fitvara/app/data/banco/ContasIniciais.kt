package com.fitvara.app.data.banco

import com.fitvara.app.data.entidades.Usuario

val CONTAS_INICIAIS = listOf(
    Usuario(
        email = "christian.pernadepau@capifit.com",
        senha = "capivara123",
        nome = "Christian",
        legenda = "Rei da subida (na teoria)",
    ),
    Usuario(
        email = "kelvi.bolhanope@capifit.com",
        senha = "cadeagua",
        nome = "Kelvi",
        legenda = "Colecionador de bolhas no pé",
    ),
    Usuario(
        email = "ryann.perdidonamata@capifit.com",
        senha = "gpsquebrou",
        nome = "Ryann",
        legenda = "Se perde até seguindo a placa",
    ),
)

suspend fun criarContasIniciais(banco: BancoCapiFit) {
    for (conta in CONTAS_INICIAIS) {
        val existente = banco.usuarioDao().buscarPorEmail(conta.email)
        if (existente == null) {
            banco.usuarioDao().inserir(conta)
        }
    }
}
