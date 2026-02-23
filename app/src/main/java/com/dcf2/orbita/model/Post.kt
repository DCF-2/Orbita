package com.example.orbita.model

import com.google.firebase.Timestamp

data class Post(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val titulo: String = "",
    val descricao: String = "",
    val dataObservacao: Timestamp = Timestamp.now(),
    val likes: Int = 0
)