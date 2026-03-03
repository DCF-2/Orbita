package com.dcf2.orbita.api.nasa

import com.dcf2.orbita.BuildConfig
import com.dcf2.orbita.model.ApodModel
import com.dcf2.orbita.model.DonkiModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface NasaApiService {
    @GET("planetary/apod")
    suspend fun getRandomAstronomyPictures(
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY,
        @Query("count") count: Int = 5
    ): List<ApodModel>

    @GET("DONKI/CME")
    suspend fun getSpaceWeatherEvents(
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): List<DonkiModel>

    @GET("neo/rest/v1/feed/today")
    suspend fun getNearEarthObjects(
        @Query("detailed") detailed: Boolean = false,
        @Query("api_key") apiKey: String = BuildConfig.NASA_API_KEY
    ): NeoWsResponse

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

data class NeoWsResponse(
    val element_count: Int,
    val near_earth_objects: Map<String, List<NeoModel>>
)

data class NeoModel(
    val id: String,
    val name: String,
    val nasa_jpl_url: String,
    val estimated_diameter: Diameter,
    val is_potentially_hazardous_asteroid: Boolean,
    val close_approach_data: List<CloseApproach>
)

data class Diameter(val kilometers: DiameterRange)
data class DiameterRange(val estimated_diameter_min: Double, val estimated_diameter_max: Double)
data class CloseApproach(val close_approach_date_full: String, val relative_velocity: Velocity, val miss_distance: Distance)
data class Velocity(val kilometers_per_hour: String)
data class Distance(val kilometers: String)