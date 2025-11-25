package com.dcf2.orbita

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.dcf2.orbita.model.*

class MainViewModel : ViewModel() {

    // --- FEED (Diário) ---
    // Inicializa com dados falsos
    private val _observacoes = getFakeObservacoes().toMutableStateList()

    // Exposta como lista imutável para a UI ler
    val observacoes: List<Observacao>
        get() = _observacoes

    fun addObservacao(titulo: String, descricao: String) {
        val novoId = _observacoes.size + 1
        val novaObs = Observacao(
            id = novoId,
            titulo = titulo,
            descricao = descricao,
            data = "Agora",
            autor = "Você"
        )
        _observacoes.add(0, novaObs)
    }

    fun removeObservacao(obs: Observacao) {
        _observacoes.remove(obs)
    }

    // --- EXPLORAR: Eventos Astronômicos ---
    val eventos = listOf(
        EventoAstronomico(1, "Eclipse Lunar Total", "14 Mar 2025", "A Lua ficará vermelha visível em todo o Brasil.", TipoEvento.ECLIPSE),
        EventoAstronomico(2, "Chuva de Líridas", "22 Abr 2025", "Pico da chuva de meteoros, melhor visível na madrugada.", TipoEvento.METEOROS),
        EventoAstronomico(3, "Oposição de Saturno", "08 Set 2025", "Saturno estará mais brilhante e próximo da Terra.", TipoEvento.CONJUNCAO)
    )

    // --- EXPLORAR: Curiosidades ---
    val curiosidades = listOf(
        Curiosidade(1, "Júpiter", "O Gigante Gasoso", "Planeta", Color(0xFFE67E22)),
        Curiosidade(2, "Betelgeuse", "Prestes a explodir?", "Estrela", Color(0xFFC0392B)),
        Curiosidade(3, "Buracos Negros", "Onde a luz não escapa", "Cosmos", Color(0xFF8E44AD)),
        Curiosidade(4, "Vias Lácteas", "Nossa casa galáctica", "Galáxia", Color(0xFF2980B9))
    )

    // --- PERFIL DO USUÁRIO ---
    var usuario by mutableStateOf(
        UsuarioPerfil(
            nome = "Astronauta Iniciante",
            handle = "@astro_dev",
            nivel = 1,
            xp = 150,
            bio = "Apaixonado pelo céu noturno e programação. Buscando minha primeira nebulosa!"
        )
    )
}

// Função auxiliar para gerar o feed inicial
fun getFakeObservacoes() = listOf(
    Observacao(1, "Lua Cheia", "Estava brilhante hoje!", "24/10/2025", "Davi"),
    Observacao(2, "Júpiter", "Vi as luas galileanas.", "23/10/2025", "NASA Fan"),
    Observacao(3, "Cruzeiro do Sul", "Muito nítido no horizonte.", "22/10/2025", "AstroBoy")
)