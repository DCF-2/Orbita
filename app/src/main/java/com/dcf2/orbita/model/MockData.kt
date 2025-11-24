package com.dcf2.orbita

import com.dcf2.orbita.model.Observacao
import com.dcf2.orbita.model.Usuario

object MockData {
    // Usuários Fake
    private val userEu = Usuario("Você", "@meu_usuario", 0, 100)
    private val user1 = Usuario("Davi", "@davi_space", 0, 100)
    private val user2 = Usuario("NASA Fan", "@nasa_lover", 0, 500)

    // Lista Mutável (Podemos adicionar coisas nela)
    val posts = mutableListOf(
        Observacao(1, user1, "Olha essa Lua!", "Estava muito brilhante hoje.", "24/10/2025", 0, 10),
        Observacao(2, user2, "Passagem da ISS", "Passou muito rápido no horizonte.", "23/10/2025", 0, 55),
        Observacao(3, user1, "Minha primeira estrela", "Acho que é Sirius.", "22/10/2025", 0, 2),
        Observacao(4, user2, "Júpiter visível", "Consegui ver a olho nu.", "21/10/2025", 0, 120)
    )

    // Função para adicionar um novo post no topo da lista
    fun adicionarPost(titulo: String, descricao: String) {
        val novoId = posts.size + 1
        val novoPost = Observacao(
            id = novoId,
            usuario = userEu, // Usa o "seu" usuário
            titulo = titulo,
            descricao = descricao,
            data = "Agora",
            fotoCeuResId = 0,
            likes = 0
        )
        posts.add(0, novoPost) // Adiciona no início da lista (índice 0)
    }
}