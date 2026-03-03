package com.dcf2.orbita.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcf2.orbita.api.iss.IssApi
import com.dcf2.orbita.api.nasa.NasaApiService
import com.dcf2.orbita.api.nasa.NeoModel
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

    var apodList by mutableStateOf<List<ApodModel>>(emptyList())
        private set

    var isLoadingApod by mutableStateOf(false)
        private set

    private val nasaApi = NasaApiService.create()

    var spaceWeatherEvents by mutableStateOf<List<DonkiModel>>(emptyList())
        private set

    var nearEarthObjects by mutableStateOf<List<NeoModel>>(emptyList())
        private set

    init {
        startTrackingIss()
        fetchPosts()
        fetchCurrentUser()
        fetchNasaApod()
    }

    // FUNÇÃO QUE CARREGA OU CRIA O USUÁRIO REAL
    fun fetchCurrentUser() {
        val fbUser = FirebaseAuth.getInstance().currentUser
        if (fbUser != null) {
            viewModelScope.launch {
                var user = userRepository.getUser(fbUser.uid)
                if (user == null) {
                    user = Usuario(
                        id = fbUser.uid,
                        nome = fbUser.displayName ?: "Astronauta Sem Nome",
                        email = fbUser.email ?: "",
                        avatarUrl = fbUser.photoUrl?.toString() ?: ""
                    )
                    userRepository.saveUser(user)
                }
                usuarioLogado = user
            }
        }
    }

    fun fetchSpaceWeather() {
        viewModelScope.launch {
            try {
                spaceWeatherEvents = nasaApi.getSpaceWeatherEvents().take(5)
            } catch (e: Exception) {
                Log.e("NASA_API", "Erro DONKI: ${e.message}")
            }
        }
    }

    fun fetchNearEarthObjects() {
        viewModelScope.launch {
            try {
                val response = nasaApi.getNearEarthObjects()
                // Pega todos os objetos de todas as datas (geralmente só hoje no feed)
                val objects = response.near_earth_objects.values.flatten().take(5)
                nearEarthObjects = objects
            } catch (e: Exception) {
                Log.e("NASA_API", "Erro NeoWs: ${e.message}")
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

    // --- LÓGICA DA NASA (APOD) ---
    fun fetchNasaApod() {
        if (isLoadingApod) return
        viewModelScope.launch {
            isLoadingApod = true
            try {
                val result = nasaApi.getRandomAstronomyPictures(count = 5)
                val imagens = result.filter { it.tipoMidia == "image" }
                apodList = apodList + imagens
            } catch (e: Exception) {
                Log.e("NASA_API", "Erro APOD: ${e.message}")
            } finally {
                isLoadingApod = false
            }
        }
    }

    fun atualizarPerfil(novoNome: String, novaBio: String) {
        val currentUser = usuarioLogado ?: return
        val updatedUser = currentUser.copy(nome = novoNome, bio = novaBio)
        viewModelScope.launch {
            val sucesso = userRepository.saveUser(updatedUser)
            if (sucesso) usuarioLogado = updatedUser
        }
    }

    fun atualizarFotoPerfil(imageUri: Uri, context: Context) {
        viewModelScope.launch {
            val currentUser = usuarioLogado ?: return@launch
            val urlDaFoto = ImageRepository.uploadImage(imageUri, context)
            if (urlDaFoto != null) {
                val updatedUser = currentUser.copy(avatarUrl = urlDaFoto)
                val sucesso = userRepository.saveUser(updatedUser)
                if (sucesso) usuarioLogado = updatedUser
            }
        }
    }

    fun atualizarLocalizacaoUsuario(lat: Double, lng: Double) {
        val currentUser = usuarioLogado ?: return
        viewModelScope.launch {
            userRepository.updateUserLocation(currentUser.id, lat, lng)
            usuarioLogado = currentUser.copy(latitude = lat, longitude = lng)
        }
    }

    fun criarPost(titulo: String, descricao: String, imageUri: Uri?, latitude: Double?, longitude: Double?, context: Context) {
        viewModelScope.launch {
            isUploading = true
            var urlDaFoto: String? = null
            if (imageUri != null) {
                urlDaFoto = ImageRepository.uploadImage(imageUri, context)
            }
            val currentUser = usuarioLogado ?: return@launch
            val novoPost = Post(
                userId = currentUser.id,
                userName = currentUser.nome,
                userAvatar = currentUser.avatarUrl,
                titulo = titulo,
                descricao = descricao,
                fotoUrl = urlDaFoto,
                likes = 0,
                latitude = latitude,
                longitude = longitude
            )
            val sucesso = journalRepository.addPost(novoPost)
            if (sucesso) fetchPosts()
            isUploading = false
        }
    }
}