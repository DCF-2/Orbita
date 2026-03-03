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
import com.dcf2.orbita.repository.UserRepository
import com.example.orbita.repository.JournalRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainViewModel : ViewModel() {

    // ---- REPOSITÓRIOS ----
    private val journalRepository = JournalRepository()
    private val userRepository = UserRepository()

    // --- ESTADO DO USUÁRIO LOGADO ---
    var usuarioLogado by mutableStateOf<Usuario?>(null)
        private set

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
        fetchPosts()
        fetchCurrentUser()
    }

    // FUNÇÃO QUE CARREGA OU CRIA O USUÁRIO REAL
    fun fetchCurrentUser() {
        val fbUser = FirebaseAuth.getInstance().currentUser
        if (fbUser != null) {
            viewModelScope.launch {
                // Tenta buscar no banco
                var user = userRepository.getUser(fbUser.uid)

                // Se for a primeira vez que o usuário loga, ele não existe no Firestore ainda
                if (user == null) {
                    user = Usuario(
                        id = fbUser.uid,
                        nome = fbUser.displayName ?: "Astronauta Sem Nome",
                        email = fbUser.email ?: "",
                        avatarUrl = fbUser.photoUrl?.toString() ?: ""
                    )
                    // Salva no banco para a próxima vez
                    userRepository.saveUser(user)
                }

                // Atualiza o estado da UI com o usuário real!
                usuarioLogado = user
            }
        }
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
    fun criarPost(titulo: String, descricao: String, imageUri: Uri?, latitude: Double?, longitude: Double?, context: Context) {
        viewModelScope.launch {
            isUploading = true

            var urlDaFoto: String? = null
            if (imageUri != null) {
                urlDaFoto = ImageRepository.uploadImage(imageUri, context)
                Log.d("OrbitaApp", "Upload finalizado: $urlDaFoto")
            }

            // SEGURANÇA: Só cria post se o usuário estiver logado
            val currentUser = usuarioLogado ?: return@launch

            val novoPost = Post(
                userId = currentUser.id, // AGORA É REAL
                userName = currentUser.nome, // AGORA É REAL
                userAvatar = currentUser.avatarUrl, // AGORA É REAL (Foto do Google!)
                titulo = titulo,
                descricao = descricao,
                fotoUrl = urlDaFoto,
                likes = 0,
                latitude = latitude,
                longitude = longitude
            )

            val sucesso = journalRepository.addPost(novoPost)

            if (sucesso) {
                Log.d("OrbitaApp", "Salvo no Firestore com sucesso!")
                fetchPosts()
            } else {
                Log.e("OrbitaApp", "Erro ao salvar no Firestore")
            }
            isUploading = false
        }
    }

    val eventos = listOf(
        EventoAstronomico(1, "Eclipse Lunar", "14 Mar 2025", "Lua Vermelha", TipoEvento.ECLIPSE),
        EventoAstronomico(2, "Chuva de Líridas", "22 Abr 2025", "Meteoros.", TipoEvento.METEOROS)
    )

    val curiosidades = listOf(
        Curiosidade(1, "Júpiter", "O Gigante Gasoso", "Planeta", Color(0xFFE67E22)),
        Curiosidade(2, "Betelgeuse", "Prestes a explodir?", "Estrela", Color(0xFFC0392B)),
        Curiosidade(3, "Buracos Negros", "Sem retorno", "Cosmos", Color(0xFF8E44AD)),
        Curiosidade(4, "Via Láctea", "Nossa casa", "Galáxia", Color(0xFF2980B9))
    )
}