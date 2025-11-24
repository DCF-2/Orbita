package com.dcf2.orbita.model

data class Usuario(
    val nome: String,
    val username: String,
    val fotoPerfilResId: Int, // Usaremos um ID de recurso (R.drawable.x) por enquanto
    val xp: Int
)