package com.example.viagemapp.repository


import com.example.viagemapp.api.GeminiService
import com.example.viagemapp.dao.RoteiroDao
import com.example.viagemapp.entity.Roteiro

class RoteiroRepository(
    private val roteiroDao: RoteiroDao,
    private val geminiService: GeminiService
) {

    suspend fun obterRoteiro(destino: String): Roteiro {
        val roteiroSalvo = roteiroDao.buscarUltimoNaoAceito(destino)
        return roteiroSalvo ?: run {
            val sugestao = geminiService.sugerirRoteiroComSalvamento(destino, this)
            val novo = Roteiro(destino = destino, sugestao = sugestao, aceito = false)
            roteiroDao.inserir(novo)
            novo
        }
    }

    suspend fun recusarERetornarOutro(destino: String): Roteiro {
        val novaSugestao = geminiService.sugerirRoteiroComSalvamento(destino, this)
        val novo = Roteiro(destino = destino, sugestao = novaSugestao, aceito = false)
        roteiroDao.inserir(novo)
        return novo
    }

    suspend fun salvar(destino: String, resultado: String) {
        val roteiro = Roteiro(destino = destino, sugestao = resultado, aceito = false)
        roteiroDao.inserir(roteiro)
    }

    suspend fun aceitarRoteiro(roteiro: Roteiro) {
        val roteiroAceito = roteiro.copy(aceito = true)
        roteiroDao.atualizar(roteiroAceito)
    }

    suspend fun listarTodos(): List<Roteiro> {
        return roteiroDao.buscarTodos()
    }
}