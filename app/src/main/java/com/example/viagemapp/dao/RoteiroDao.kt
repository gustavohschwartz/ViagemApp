package com.example.viagemapp.dao

import androidx.room.*
import com.example.viagemapp.entity.Roteiro



@Dao
interface RoteiroDao {
    @Query("SELECT * FROM roteiros WHERE destino = :destino AND aceito = 0 ORDER BY id DESC LIMIT 1")
    suspend fun buscarUltimoNaoAceito(destino: String): Roteiro?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun inserir(roteiro: Roteiro)
    @Update
    suspend fun atualizar(roteiro: Roteiro)
    @Query("SELECT * FROM roteiros ORDER BY id DESC")
    suspend fun buscarTodos(): List<Roteiro>

}

