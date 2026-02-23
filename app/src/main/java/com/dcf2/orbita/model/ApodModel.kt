package com.example.orbita.model

import com.google.gson.annotations.SerializedName

data class ApodModel(
    @SerializedName("title") val titulo: String,
    @SerializedName("explanation") val explicacao: String,
    @SerializedName("url") val imagemUrl: String,
    @SerializedName("date") val data: String,
    @SerializedName("media_type") val tipoMidia: String // "image" ou "video"
)