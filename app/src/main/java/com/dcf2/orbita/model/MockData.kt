package com.dcf2.orbita

import androidx.compose.runtime.toMutableStateList
import com.dcf2.orbita.model.Observacao

object MockData {
    // Lista Mutável (Podemos adicionar coisas nela)
    // Agora usando o construtor correto: (id, titulo, descricao, data, autor)
    val posts = mutableListOf(
        Observacao(1, "Olha essa Lua!", "Estava muito brilhante hoje.", "24/10/2025", "Davi"),
        Observacao(2, "Passagem da ISS", "Passou muito rápido no horizonte.", "23/10/2025", "NASA Fan"),
        Observacao(3, "Minha primeira estrela", "Acho que é Sirius.", "22/10/2025", "Davi"),
        Observacao(4, "Júpiter visível", "Consegui ver a olho nu.", "21/10/2025", "NASA Fan")
    )

    // Função para adicionar um novo post no topo da lista
    fun adicionarPost(titulo: String, descricao: String) {
        val novoId = posts.size + 1
        val novoPost = Observacao(
            id = novoId,
            titulo = titulo,
            descricao = descricao,
            data = "Agora",
            autor = "Você" // Simplificado para String
        )
        posts.add(0, novoPost) // Adiciona no início da lista
    }
}