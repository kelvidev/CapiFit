package com.fitvara.app.data.banco

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRACAO_1_PARA_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `usuarios` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`email` TEXT NOT NULL, `senha` TEXT NOT NULL, `nome` TEXT NOT NULL, `legenda` TEXT NOT NULL)",
        )
        db.execSQL(
            "INSERT INTO usuarios (id, email, senha, nome, legenda) " +
                "VALUES (1, '$EMAIL_CONVIDADO', '', '$NOME_CONVIDADO', '$LEGENDA_CONVIDADO')",
        )

        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `atividades_nova` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                "`nome` TEXT NOT NULL, `tipo` TEXT NOT NULL, `data` TEXT NOT NULL, " +
                "`duracao_min` INTEGER NOT NULL, `distancia_km` REAL NOT NULL, `desnivel_m` INTEGER NOT NULL, " +
                "`dificuldade` TEXT NOT NULL, `notas` TEXT NOT NULL, `usuario_id` INTEGER NOT NULL, " +
                "`favorita` INTEGER NOT NULL)",
        )
        db.execSQL(
            "INSERT INTO atividades_nova (id, nome, tipo, data, duracao_min, distancia_km, " +
                "desnivel_m, dificuldade, notas, usuario_id, favorita) SELECT id, nome, tipo, data, " +
                "duracao_min, distancia_km, desnivel_m, dificuldade, notas, 1, 0 FROM atividades",
        )
        db.execSQL("DROP TABLE atividades")
        db.execSQL("ALTER TABLE atividades_nova RENAME TO atividades")

        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `acessos` (`usuario_id` INTEGER NOT NULL, `data` TEXT NOT NULL, " +
                "PRIMARY KEY(`usuario_id`, `data`))",
        )
    }
}