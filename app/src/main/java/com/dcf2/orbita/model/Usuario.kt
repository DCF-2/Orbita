package com.dcf2.orbita.model

data class Usuario(
    val id: String = "",
    val nome: String = "",
    val email: String = "",
    val avatarUrl: String = "",
    val bio: String = "Explorando o universo com o Órbita.", // Bio padrão
    val nivel: Int = 1,
    val xp: Int = 0,
    // Preparando o terreno para a Issue #8
    val latitude: Double? = null,
    val longitude: Double? = null
)