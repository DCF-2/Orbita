package com.dcf2.orbita

import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import com.dcf2.orbita.model.Observacao

class MainViewModel : ViewModel() {
    // Lista observável para o Compose reagir a mudanças
    // Inicializa com dados falsos para teste
    private val _observacoes = getFakeObservacoes().toMutableStateList()

    // Exposta como lista imutável para segurança
    val observacoes: List<Observacao>
        get() = _observacoes

    // Adiciona uma nova observação
    fun addObservacao(titulo: String, descricao: String) {
        val novoId = _observacoes.size + 1
        val novaObs = Observacao(
            id = novoId,
            titulo = titulo,
            descricao = descricao,
            data = "Agora", // Futuramente, usar LocalDateTime
            autor = "Você"
        )
        _observacoes.add(0, novaObs) // Adiciona no topo da lista
    }

    // Remove uma observação
    fun removeObservacao(obs: Observacao) {
        _observacoes.remove(obs)
    }
}

// Função auxiliar para gerar dados falsos iniciais
fun getFakeObservacoes() = listOf(
    Observacao(1, "Lua Cheia", "Estava brilhante hoje!", "24/10/2025", "Davi"),
    Observacao(2, "Júpiter", "Vi as luas galileanas.", "23/10/2025", "NASA Fan"),
    Observacao(3, "Cruzeiro do Sul", "Muito nítido no horizonte.", "22/10/2025", "AstroBoy")
)