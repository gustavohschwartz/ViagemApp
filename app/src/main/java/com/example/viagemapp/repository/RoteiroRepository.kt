package com.example.viagemapp.repository

import com.example.viagemapp.api.GeminiService
import com.example.viagemapp.dao.RoteiroDao
import com.example.viagemapp.entity.Roteiro
import kotlinx.coroutines.flow.Flow

class RoteiroRepository(
    private val roteiroDao: RoteiroDao,
    private val geminiService: GeminiService
) {

    suspend fun obterRoteiro(destino: String, username: String? = null): Roteiro {
        val roteiroSalvo = roteiroDao.buscarUltimoNaoAceito(destino)
        return roteiroSalvo ?: run {
            val sugestao = geminiService.sugerirRoteiroComSalvamento(destino, this)
            val novo = Roteiro(destino = destino, sugestao = sugestao, aceito = false, username = username ?: "")
            roteiroDao.inserir(novo)
            novo
        }
    }

    suspend fun recusarERetornarOutro(destino: String, username: String? = null): Roteiro {
        val novaSugestao = geminiService.sugerirRoteiroComSalvamento(destino, this)
        val novo = Roteiro(destino = destino, sugestao = novaSugestao, aceito = false, username = username ?: "")
        roteiroDao.inserir(novo)
        return novo
    }

    suspend fun salvar(destino: String, resultado: String, username: String? = null) {
        val roteiro = Roteiro(destino = destino, sugestao = resultado, aceito = false, username = username ?: "")
        roteiroDao.inserir(roteiro)
    }

    suspend fun aceitarRoteiro(roteiro: Roteiro) {
        val roteiroAceito = roteiro.copy(aceito = true)
        roteiroDao.atualizar(roteiroAceito)
    }

    suspend fun listarTodos(): List<Roteiro> {
        return roteiroDao.buscarTodos()
    }

    // 🔹 NOVO: Deleta um roteiro
    suspend fun excluirRoteiro(roteiro: Roteiro) {
        roteiroDao.deletar(roteiro)
    }

    // 🔹 NOVO: Roteiros aceitos de um usuário
    fun roteirosDoUsuario(username: String): Flow<List<Roteiro>> {
        return roteiroDao.buscarPorUsuario(username)
    }
}
