package com.dcf2.orbita.api.iss

import com.dcf2.orbita.model.IssResponse
import retrofit2.http.GET

interface IssApi {
    @GET("iss-now.json")
    suspend fun getIssPosition(): IssResponse
}