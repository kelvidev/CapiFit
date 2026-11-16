package com.fitvara.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.fitvara.app.data.entidades.Atividade
import kotlinx.coroutines.flow.Flow

@Dao
interface AtividadeDao {

    @Query("SELECT * FROM atividades WHERE usuario_id = :usuarioId ORDER BY data DESC, id DESC")
    fun listar(usuarioId: Long): Flow<List<Atividade>>

    @Insert
    suspend fun inserir(atividade: Atividade)

    @Update
    suspend fun atualizar(atividade: Atividade)

    @Query("DELETE FROM atividades WHERE id = :id")
    suspend fun excluir(id: Long)

    @Query("DELETE FROM atividades WHERE usuario_id = :usuarioId")
    suspend fun excluirDoUsuario(usuarioId: Long)

    @Query("SELECT COUNT(*) FROM atividades WHERE usuario_id = :usuarioId and favorita = 1")
    suspend fun contarFavoritas(usuarioId: Long): Int
}
