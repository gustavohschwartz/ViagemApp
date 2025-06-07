package com.example.viagemapp.repository

import com.example.viagemapp.dao.RoteiroDao
import com.example.viagemapp.entity.Roteiro

class RoteiroRepository(private val roteiroDao: RoteiroDao) {

    suspend fun addRoteiro(roteiro: Roteiro) = roteiroDao.insert(roteiro)

    suspend fun getRoteirosByUsername(username: String): List<Roteiro> {
        return roteiroDao.getRoteirosByUsername(username)
    }

    suspend fun deleteRoteiro(roteiro: Roteiro) {
        roteiroDao.delete(roteiro)
    }
}
