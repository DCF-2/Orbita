package com.dcf2.orbita.model

import com.google.gson.annotations.SerializedName

data class IssSatellitePosition(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("altitude") val altitude: Double, // km
    @SerializedName("velocity") val velocity: Double, // km/h
    @SerializedName("visibility") val visibility: String, // "daylight" ou "eclipsed"
    @SerializedName("timestamp") val timestamp: Long
)