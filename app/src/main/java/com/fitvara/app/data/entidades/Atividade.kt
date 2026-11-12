package com.fitvara.app.data.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class TipoAtividade { Run, Trail, Mountain }

enum class Dificuldade { Facil, Media, Alta }

@Entity(tableName = "atividades")
data class Atividade(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nome: String,
    val tipo: TipoAtividade,
    val data: String,
    @ColumnInfo(name = "duracao_min")
    val duracaoMin: Int,
    @ColumnInfo(name = "distancia_km")
    val distanciaKm: Double,
    @ColumnInfo(name = "desnivel_m")
    val desnivelM: Int,
    val dificuldade: Dificuldade,
    val notas: String = "",
    @ColumnInfo(name = "usuario_id")
    val usuarioId: Long = 0,
    val favorita: Boolean = false,
)
