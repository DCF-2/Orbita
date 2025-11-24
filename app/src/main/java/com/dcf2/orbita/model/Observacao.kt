package com.dcf2.orbita.model

data class Observacao(
    val id: Int,
    val usuario: Usuario,
    val titulo: String,
    val descricao: String,
    val data: String,
    val fotoCeuResId: Int, // ID da imagem do post (simulado)
    val likes: Int
)