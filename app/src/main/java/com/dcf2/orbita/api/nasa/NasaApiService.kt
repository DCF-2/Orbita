package com.dcf2.orbita.api.nasa

import com.example.orbita.model.ApodModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    @GET("planetary/apod")
    suspend fun getAstronomyPicture(
        @Query("api_key") apiKey: String = "DEMO_KEY" // Chave pública de teste
    ): ApodModel

    // Singleton para criar a conexão (Padrão simples para o projeto acadêmico)
    companion object {
        private const val BASE_URL = "https://api.nasa.gov/"

        fun create(): NasaApiService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NasaApiService::class.java)
        }
    }
}