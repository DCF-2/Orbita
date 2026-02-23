package com.dcf2.orbita.model

import com.google.gson.annotations.SerializedName

// Esta classe representa a resposta completa da API
data class IssResponse(
    @SerializedName("iss_position") val posicao: IssPositionData,
    @SerializedName("message") val mensagem: String,
    @SerializedName("timestamp") val timestamp: Long
)

// Esta classe representa apenas as coordenadas (o "miolo" do JSON)
data class IssPositionData(
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String
)