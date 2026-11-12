package com.fitvara.app.data.entidades

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "acessos", primaryKeys = ["usuario_id", "data"])
data class Acesso(
    @ColumnInfo(name = "usuario_id")
    val usuarioId: Long,
    val data: String,
)
