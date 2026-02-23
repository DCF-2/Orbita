package com.example.orbita.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbita.model.Post
import com.example.orbita.repository.JournalRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FeedViewModel : ViewModel() {
    private val repository = JournalRepository()

    // Estado da lista de posts
    private val _posts = MutableStateFlow<List<Post>>(emptyList())
    val posts: StateFlow<List<Post>> = _posts

    // Estado de carregamento
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchPosts()
    }

    fun fetchPosts() {
        viewModelScope.launch {
            _isLoading.value = true
            _posts.value = repository.getPosts()
            _isLoading.value = false
        }
    }

    fun createPost(titulo: String, descricao: String) {
        viewModelScope.launch {
            // Exemplo simplificado. Pegue o UserID real do Firebase Auth aqui
            val newPost = Post(
                userId = "user_123",
                userName = "Astrônomo Viajante",
                titulo = titulo,
                descricao = descricao
            )
            val success = repository.addPost(newPost)
            if (success) fetchPosts() // Recarrega a lista se deu certo
        }
    }
}