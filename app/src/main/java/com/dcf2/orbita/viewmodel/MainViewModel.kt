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
import com.example.orbita.repository.JournalRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {

    // ---- REPOSITÓRIOS ----
    private val journalRepository = JournalRepository()

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
        fetchPosts() // <--- AGORA CARREGAMOS DO FIREBASE AO INICIAR
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
    private val _posts = mutableListOf<Post>().toMutableStateList()
    val posts: List<Post> get() = _posts

    // Função para buscar posts da nuvem
    fun fetchPosts() {
        viewModelScope.launch {
            try {
                val postsDaNuvem = journalRepository.getPosts()
                _posts.clear()
                _posts.addAll(postsDaNuvem)
            } catch (e: Exception) {
                Log.e("OrbitaApp", "Erro ao buscar posts: ${e.message}")
            }
        }
    }

    // FUNÇÃO PRINCIPAL: Cria o post + Upload Imagem + Salva no Firestore
    fun criarPost(titulo: String, descricao: String, imageUri: Uri?, context: Context) {
        viewModelScope.launch {
            isUploading = true // Ativa loading

            var urlDaFoto: String? = null

            // 1. Upload da Imagem (se houver) para o Cloudinary
            if (imageUri != null) {
                urlDaFoto = ImageRepository.uploadImage(imageUri, context)
                Log.d("OrbitaApp", "Upload finalizado: $urlDaFoto")
            }

            // 2. Cria o objeto Post
            val novoPost = Post(
                // O ID será gerado automaticamente se vazio, ou pelo Firestore
                userId = "user_temp_id", // Futuramente, pegaremos do Firebase Auth
                userName = usuario.nome, // Pega o nome do perfil atual
                userAvatar = "",
                titulo = titulo,
                descricao = descricao,
                fotoUrl = urlDaFoto,
                likes = 0
            )

            // 3. Salva no Firestore (O Pulo do Gato!)
            val sucesso = journalRepository.addPost(novoPost)

            if (sucesso) {
                Log.d("OrbitaApp", "Salvo no Firestore com sucesso!")
                fetchPosts() // Recarrega a lista para garantir sincronia
            } else {
                Log.e("OrbitaApp", "Erro ao salvar no Firestore")
            }

            isUploading = false // Desativa loading
        }
    }

    // --- DADOS ESTÁTICOS ---
    var usuario by mutableStateOf(
        UsuarioPerfil("Astronauta", "@astro_dev", 1, 150, "Explorando o universo.")
    )

    val eventos = listOf(
        EventoAstronomico(1, "Eclipse Lunar", "14 Mar 2025", "Lua Vermelha", TipoEvento.ECLIPSE),
        EventoAstronomico(2, "Chuva de Líridas", "22 Abr 2025", "Meteoros.", TipoEvento.METEOROS)
    )
}