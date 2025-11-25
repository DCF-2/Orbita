package com.dcf2.orbita.model

import androidx.compose.ui.graphics.Color

data class Curiosidade(
    val id: Int,
    val titulo: String,
    val subtitulo: String,
    val categoria: String, // Planeta, Estrela, etc.
    val corCartao: Color // Para dar um visual bonito na UI
)
