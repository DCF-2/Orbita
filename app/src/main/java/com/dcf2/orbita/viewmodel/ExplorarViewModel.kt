package com.example.orbita.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orbita.api.NasaApiService
import com.example.orbita.model.ApodModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExplorarViewModel : ViewModel() {
    private val api = NasaApiService.create()

    // Estado da imagem (pode ser null se estiver carregando ou der erro)
    private val _apod = MutableStateFlow<ApodModel?>(null)
    val apod: StateFlow<ApodModel?> = _apod

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        carregarImagemDoDia()
    }

    private fun carregarImagemDoDia() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // Chama a API da NASA
                val resultado = api.getAstronomyPicture()
                _apod.value = resultado
            } catch (e: Exception) {
                e.printStackTrace() // Em produção, mostrariamos um erro na tela
            } finally {
                _isLoading.value = false
            }
        }
    }
}