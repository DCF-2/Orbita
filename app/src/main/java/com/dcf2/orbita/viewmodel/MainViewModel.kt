package com.dcf2.orbita.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcf2.orbita.api.iss.IssApi
import com.dcf2.orbita.model.*
import com.dcf2.orbita.repository.ImageRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {

    // --- ESTADOS DE UI ---
    var isUploading by mutableStateOf(false)
        private set

    // --- ISS TRACKER LOGIC ---
    var issData by mutableStateOf<IssSatellitePosition?>(null)
        private set

    private val api: IssApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.wheretheiss.at/")
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
                } catch (e: Exception) {
                    Log.e("ISS_TRACKER", "Erro: ${e.message}")
                }
                delay(5000)
            }
        }
    }

    // --- LÓGICA DO DIÁRIO ---
    // Agora usamos a classe Post oficial em vez de Observacao
    private val _posts = mutableListOf<Post>().toMutableStateList()
    val posts: List<Post> get() = _posts

    init {
        // Carga inicial de dados falsos para teste
        _posts.add(Post(titulo = "Lua Cheia", descricao = "Linda noite!", userName = "Davi"))
        _posts.add(Post(titulo = "Saturno", descricao = "Vi os anéis.", userName = "Astro"))
    }

    // FUNÇÃO PRINCIPAL: Cria o post com (ou sem) imagem
    fun criarPost(titulo: String, descricao: String, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            isUploading = true // Ativa loading na tela

            var urlDaFoto: String? = null

            // 1. Se tiver imagem, faz upload primeiro
            if (imageUri != null) {
                urlDaFoto = ImageRepository.uploadImage(imageUri, context)
            }

            // 2. Cria o objeto Post
            val novoPost = Post(
                id = System.currentTimeMillis().toString(),
                userId = "user_temp",
                userName = "Você",
                titulo = titulo,
                descricao = descricao,
                fotoUrl = urlDaFoto, // Salva a URL do Cloudinary aqui
                likes = 0
            )

            // 3. Adiciona à lista (futuramente salvará no Firestore aqui)
            _posts.add(0, novoPost)

            isUploading = false // Desativa loading
        }
    }

    // --- DADOS ESTÁTICOS (Mantidos) ---
    val eventos = listOf(
        EventoAstronomico(1, "Eclipse Lunar", "14 Mar 2025", "Lua Vermelha", TipoEvento.ECLIPSE),
        EventoAstronomico(2, "Chuva de Líridas", "22 Abr 2025", "Meteoros.", TipoEvento.METEOROS)
    )

    val curiosidades = listOf(
        Curiosidade(1, "Júpiter", "Gigante", "Planeta", Color(0xFFE67E22)),
        Curiosidade(2, "Betelgeuse", "Supernova", "Estrela", Color(0xFFC0392B))
    )

    var usuario by mutableStateOf(
        UsuarioPerfil("Astronauta", "@astro_dev", 1, 150, "Explorando.")
    )
}