package com.example.viagemapp.screens

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.viagemapp.entity.Roteiro
import com.example.viagemapp.repository.RoteiroRepository
import kotlinx.coroutines.launch

class RoteiroViewModel(private val repository: RoteiroRepository) : ViewModel() {
    private val _roteiros = mutableStateListOf<Roteiro>()
    val roteiros: List<Roteiro> get() = _roteiros

    fun carregarRoteiros(username: String) {
        viewModelScope.launch {
            val lista = repository.getRoteirosByUsername(username)
            _roteiros.clear()
            _roteiros.addAll(lista)

        }
    }


    fun salvarRoteiro(username: String, destino: String, sugestao: String) {
        viewModelScope.launch {
            val roteiro = Roteiro(username = username, destino = destino, sugestao = sugestao)
            repository.addRoteiro(roteiro)
            _roteiros.add(roteiro)
        }
    }

    fun deletarRoteiro(roteiro: Roteiro) {
        viewModelScope.launch {
            repository.deleteRoteiro(roteiro)
        }
    }

}
