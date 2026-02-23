package com.dcf2.orbita.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lens
import androidx.compose.material.icons.filled.North
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dcf2.orbita.viewmodel.MainViewModel

@Composable
fun ObservatorioPage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF020024), Color(0xFF090979)) // Degradê céu noturno
                )
            )
    ) {
        // Simulação de Estrelas (Pontos brancos)
        // Num app real, isso seria a Preview da Câmera
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Lens,
                contentDescription = "Mira",
                tint = Color.White.copy(alpha = 0.5f),
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text("Aponte para o céu", color = Color.White.copy(alpha = 0.7f))
        }

        // HUD (Head-Up Display) - Informações na tela
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Modo: Identificação AR", color = Color.Green, fontWeight = FontWeight.Bold)
            Text("Azimute: 145° SE", color = Color.White)
            Text("Elevação: 45°", color = Color.White)
        }

        // Bússola Simulada
        Icon(
            imageVector = Icons.Default.North,
            contentDescription = "Norte",
            tint = Color.Red,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
                .size(40.dp)
                .border(1.dp, Color.White, CircleShape)
                .padding(4.dp)
        )

        // Botão de "Em Breve"
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(16.dp)
        ) {
            Text(
                "🔭 Recurso AR em desenvolvimento\nIntegração com Câmera e Acelerômetro em breve.",
                color = Color.White,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}