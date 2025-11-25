package com.dcf2.orbita.model

data class EventoAstronomico(
    val id: Int,
    val titulo: String,
    val data: String,
    val descricao: String,
    val tipo: TipoEvento
)