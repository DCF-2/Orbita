package com.dcf2.orbita.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcf2.orbita.viewmodel.MainViewModel

@Composable
fun MapPage(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B14)) // Fundo escuro
    ) {
        // Título
        Text(
            text = "Mapa de Eventos",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        // Simulação do Mapa (Um quadrado verde/azul representando o mapa)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color(0xFF1E293B), shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Mapa",
                    tint = Color(0xFFF2994A),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Google Maps em Breve",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Aqui você verá locais de observação",
                    color = Color.Gray
                )
            }
        }
    }
}