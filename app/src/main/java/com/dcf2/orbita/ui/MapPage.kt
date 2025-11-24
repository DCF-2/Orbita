package com.dcf2.orbita.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.dcf2.orbita.MainViewModel

@Composable
fun MapPage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    // Placeholder para o Google Maps (simulando a estrutura do WeatherApp)
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Mapa do Observatório \n(Em breve com Google Maps)", color = Color.White, fontSize = 18.sp)
    }
}