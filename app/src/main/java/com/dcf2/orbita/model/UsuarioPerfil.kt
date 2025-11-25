package com.dcf2.orbita.model

data class UsuarioPerfil(
    val nome: String,
    val handle: String, // @usuario
    val nivel: Int,
    val xp: Int,
    val bio: String,
)