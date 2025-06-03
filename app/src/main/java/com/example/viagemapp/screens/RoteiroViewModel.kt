package com.example.viagemapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viagemapp.api.GeminiService
import com.example.viagemapp.entity.Roteiro
import com.example.viagemapp.repository.RoteiroRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class RoteiroViewModel(
    private val repository: RoteiroRepository,
    private val geminiService: GeminiService
) : ViewModel() {

    private val _roteiro = MutableStateFlow<Roteiro?>(null)
    val roteiro: StateFlow<Roteiro?> get() = _roteiro

    private val _roteiros = MutableStateFlow<List<Roteiro>>(emptyList())
    val roteiros: StateFlow<List<Roteiro>> get() = _roteiros

    // Carrega um roteiro salvo ou sugere novo se não houver
    fun carregarRoteiro(destino: String) {
        viewModelScope.launch {
            val roteiroCarregado = repository.obterRoteiro(destino)
            _roteiro.value = roteiroCarregado
        }
    }

    // Aceita e salva o roteiro no banco
    fun aceitarRoteiro(roteiro: Roteiro) {
        viewModelScope.launch {
            repository.aceitarRoteiro(roteiro)
            _roteiro.value = roteiro.copy(aceito = true)
        }
    }

    // Gera nova sugestão e substitui a atual
    fun recusarERetornarOutro(destino: String) {
        viewModelScope.launch {
            val novoRoteiro = repository.recusarERetornarOutro(destino)
            _roteiro.value = novoRoteiro
        }
    }

    // Carrega todos os roteiros (para uso geral ou admin)
    fun carregarTodosRoteiros() {
        viewModelScope.launch {
            _roteiros.value = try {
                repository.listarTodos()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    // 🔹 Carrega os roteiros aceitos de um usuário específico
    fun roteirosDoUsuario(username: String): StateFlow<List<Roteiro>> {
        return repository.roteirosDoUsuario(username)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    }

    // 🔹 Exclui um roteiro do banco de dados
    fun excluirRoteiro(roteiro: Roteiro) {
        viewModelScope.launch {
            repository.excluirRoteiro(roteiro)
            carregarTodosRoteiros()
        }
    }
}
