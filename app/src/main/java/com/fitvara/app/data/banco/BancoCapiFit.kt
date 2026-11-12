package com.fitvara.app.data.banco

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fitvara.app.data.dao.AcessoDao
import com.fitvara.app.data.dao.AtividadeDao
import com.fitvara.app.data.dao.UsuarioDao
import com.fitvara.app.data.entidades.Acesso
import com.fitvara.app.data.entidades.Atividade
import com.fitvara.app.data.entidades.Usuario

@Database(
    entities = [Usuario::class, Atividade::class, Acesso::class],
    version = 2,
    exportSchema = false,
)
abstract class BancoCapiFit : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun atividadeDao(): AtividadeDao

    abstract fun acessoDao(): AcessoDao

    companion object {
        fun abrir(context: Context): BancoCapiFit {
            return Room.databaseBuilder(context, BancoCapiFit::class.java, "capifit.db")
                .addMigrations(MIGRACAO_1_PARA_2)
                .build()
        }
    }
}
