package com.dcf2.orbita.api.iss

import com.dcf2.orbita.model.IssPosition
import retrofit2.http.GET

interface IssApi {
    @GET("v1/satellites/25544")
    suspend fun getPosition(): IssPosition
}