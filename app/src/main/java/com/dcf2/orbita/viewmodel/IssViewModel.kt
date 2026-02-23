package com.dcf2.orbita.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dcf2.orbita.api.iss.IssApi
import com.dcf2.orbita.model.IssResponse
import com.dcf2.orbita.utils.MathUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class IssViewModel : ViewModel() {

    private val api: IssApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://api.open-notify.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IssApi::class.java)
    }

    // Agora usamos IssResponse, que é o objeto completo
    private val _issResponse = MutableStateFlow<IssResponse?>(null)
    val issResponse: StateFlow<IssResponse?> = _issResponse

    private val _userLocation = MutableStateFlow<Pair<Double, Double>?>(null)
    val userLocation: StateFlow<Pair<Double, Double>?> = _userLocation

    private val _distancia = MutableStateFlow<Double?>(null)
    val distancia: StateFlow<Double?> = _distancia

    init {
        iniciarRastreamento()
    }

    fun atualizarLocalizacaoUsuario(lat: Double, lon: Double) {
        _userLocation.value = lat to lon
        recalcularDistancia()
    }

    private fun iniciarRastreamento() {
        viewModelScope.launch {
            while (isActive) {
                try {
                    // Chama a API corrigida
                    val response = api.getIssPosition()
                    _issResponse.value = response
                    recalcularDistancia()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                delay(5000)
            }
        }
    }

    private fun recalcularDistancia() {
        val response = _issResponse.value
        val user = _userLocation.value

        if (response != null && user != null) {
            // Acessa .posicao (definido no IssResponse) em vez de .iss_position
            val latIss = response.posicao.latitude.toDouble()
            val lonIss = response.posicao.longitude.toDouble()

            val dist = MathUtils.calcularDistancia(
                user.first, user.second,
                latIss, lonIss
            )
            _distancia.value = dist
        }
    }
}