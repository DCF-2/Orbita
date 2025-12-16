package com.dcf2.orbita.model

// Essa classe representa os dados que vêm da API
data class IssPosition(
    val latitude: Double,
    val longitude: Double,
    val altitude: Double,
    val velocity: Double,
    val visibility: String,
    val timestamp: Long
)