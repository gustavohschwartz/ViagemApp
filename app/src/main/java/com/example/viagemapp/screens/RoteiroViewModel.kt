package com.example.viagemapp.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viagemapp.api.GeminiService
import com.example.viagemapp.entity.Roteiro
import com.example.viagemapp.repository.RoteiroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoteiroViewModel(
    private val repository: RoteiroRepository,
    private val geminiService: GeminiService
) : ViewModel() {

    private val _roteiro = MutableStateFlow<Roteiro?>(null)
    val roteiro: StateFlow<Roteiro?> get() = _roteiro

    private val _roteiros = MutableStateFlow<List<Roteiro>>(emptyList())
    val roteiros: StateFlow<List<Roteiro>> get() = _roteiros

    // Carrega roteiro salvo ou gera um novo
    fun carregarRoteiro(destino: String) {
        viewModelScope.launch {
            val roteiroCarregado = repository.obterRoteiro(destino)
            _roteiro.value = roteiroCarregado
        }
    }

    // Salva um novo roteiro baseado no destino (usa lógica interna do repository)
    fun salvarRoteiro(destino: String) {
        viewModelScope.launch {
            val roteiroSalvo = repository.obterRoteiro(destino)
            _roteiro.value = roteiroSalvo
        }
    }

    // Recusa roteiro e busca outro
    fun recusarERetornarOutro(destino: String) {
        viewModelScope.launch {
            val novoRoteiro = repository.recusarERetornarOutro(destino)
            _roteiro.value = novoRoteiro
        }
    }

    // Aceita o roteiro e atualiza no banco
    fun aceitarRoteiro(roteiro: Roteiro) {
        viewModelScope.launch {
            repository.aceitarRoteiro(roteiro)
            _roteiro.value = roteiro.copy(aceito = true)
        }
    }

    // Carrega todos os roteiros salvos no banco
    fun carregarTodosRoteiros() {
        viewModelScope.launch {
            try {
                val lista = repository.listarTodos()
                _roteiros.value = lista
            } catch (e: Exception) {
                _roteiros.value = emptyList() // Em caso de erro, retornar lista vazia
            }
        }
    }
}