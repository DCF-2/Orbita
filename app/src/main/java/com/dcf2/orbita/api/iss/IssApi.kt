package com.dcf2.orbita.api.iss

import com.dcf2.orbita.model.IssSatellitePosition
import retrofit2.http.GET
import retrofit2.http.Query

interface IssApi {
    // Endpoint: https://api.wheretheiss.at/v1/satellites/25544
    @GET("v1/satellites/25544")
    suspend fun getIssPosition(
        @Query("units") units: String = "kilometers" // Pede dados em km
    ): IssSatellitePosition
}