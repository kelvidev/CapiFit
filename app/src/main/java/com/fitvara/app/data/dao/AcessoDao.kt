package com.fitvara.app.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fitvara.app.data.entidades.Acesso
import kotlinx.coroutines.flow.Flow

@Dao
interface AcessoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun registrar(acesso: Acesso)

    @Query("SELECT data FROM acessos WHERE usuario_id = :usuarioId")
    fun listarDias(usuarioId: Long): Flow<List<String>>

    @Query("DELETE FROM acessos WHERE usuario_id = :usuarioId")
    suspend fun excluirDoUsuario(usuarioId: Long)
}
