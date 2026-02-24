package com.dcf2.orbita.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcf2.orbita.model.*
import com.dcf2.orbita.api.iss.IssApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {

    // --- ISS TRACKER LOGIC ---
    // Agora usamos o novo modelo IssSatellitePosition
    var issData by mutableStateOf<IssSatellitePosition?>(null)
        private set

    private val api: IssApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.wheretheiss.at/") // URL Segura (HTTPS)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IssApi::class.java)
    }

    init {
        startTrackingIss()
    }

    private fun startTrackingIss() {
        viewModelScope.launch {
            while (true) {
                try {
                    val result = api.getIssPosition()
                    issData = result
                    Log.d("ISS_TRACKER", "Dados recebidos: Alt=${result.altitude}, Vel=${result.velocity}")
                } catch (e: Exception) {
                    Log.e("ISS_TRACKER", "Erro: ${e.message}")
                }
                delay(3000) // Atualiza a cada 3 segundos (API permite até 1req/s)
            }
        }
    }

    // --- LÓGICA DO DIÁRIO (Mantida igual) ---
    private val _observacoes = getFakeObservacoes().toMutableStateList()
    val observacoes: List<Observacao> get() = _observacoes

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

    // --- DADOS ESTÁTICOS (Mantidos iguais) ---
    val eventos = listOf(
        EventoAstronomico(1, "Eclipse Lunar Total", "14 Mar 2025", "A Lua ficará vermelha.", TipoEvento.ECLIPSE),
        EventoAstronomico(2, "Chuva de Líridas", "22 Abr 2025", "Pico da chuva de meteoros.", TipoEvento.METEOROS),
        EventoAstronomico(3, "Oposição de Saturno", "08 Set 2025", "Saturno estará mais brilhante.", TipoEvento.CONJUNCAO)
    )

    val curiosidades = listOf(
        Curiosidade(1, "Júpiter", "O Gigante Gasoso", "Planeta", Color(0xFFE67E22)),
        Curiosidade(2, "Betelgeuse", "Prestes a explodir?", "Estrela", Color(0xFFC0392B)),
        Curiosidade(3, "Buracos Negros", "Sem retorno", "Cosmos", Color(0xFF8E44AD)),
        Curiosidade(4, "Vias Lácteas", "Nossa casa", "Galáxia", Color(0xFF2980B9))
    )

    var usuario by mutableStateOf(
        UsuarioPerfil("Astronauta", "@astro_dev", 1, 150, "Explorando o universo.")
    )
}

fun getFakeObservacoes() = listOf(
    Observacao(1, "Lua Cheia", "Estava brilhante hoje!", "24/10/2025", "Davi"),
    Observacao(2, "Passagem da ISS", "Passou muito rápido.", "23/10/2025", "NASA Fan"),
    Observacao(3, "Cruzeiro do Sul", "Muito nítido.", "22/10/2025", "AstroBoy")
)