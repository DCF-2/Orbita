package com.dcf2.orbita.ui

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState // Importante para rolar
import androidx.compose.foundation.verticalScroll     // Importante para rolar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.dcf2.orbita.MainViewModel

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ISSTrackerPage(viewModel: MainViewModel) {
    val issData = viewModel.issPosition

    // Estado de rolagem para telas pequenas
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B14))
            .verticalScroll(scrollState) // <--- CORREÇÃO 1: Permite rolar a tela
    ) {

        // 1. Modelo 3D (WebView)
        Box(
            modifier = Modifier
                .height(320.dp) // <--- CORREÇÃO 2: Diminuí um pouco para sobrar espaço
                .fillMaxWidth()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = Color(0xFF2D9CDB))
                Spacer(modifier = Modifier.height(8.dp))
                Text("Carregando modelo 3D...", color = Color.Gray, fontSize = 12.sp)
            }

            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = { context ->
                    WebView(context).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.javaScriptEnabled = true
                        settings.domStorageEnabled = true
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        setBackgroundColor(0x00000000)
                        webViewClient = WebViewClient()

                        // <--- CORREÇÃO 3: ID correto da ISS (2378)
                        loadUrl("https://solarsystem.nasa.gov/gltf_embed/2378")
                    }
                }
            )
        }

        // 2. Painel de Informações
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Estação Espacial Internacional",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (issData != null) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("●", color = Color.Green, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Dados em tempo real", color = Color.Green, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                InfoCard("Latitude", String.format("%.4f°", issData.latitude))
                InfoCard("Longitude", String.format("%.4f°", issData.longitude))
                InfoCard("Altitude", String.format("%.2f km", issData.altitude))
                InfoCard("Velocidade", String.format("%.0f km/h", issData.velocity))

                // Espaço extra no final para não cortar o último card
                Spacer(modifier = Modifier.height(32.dp))
            } else {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    color = Color(0xFF2D9CDB),
                    trackColor = Color(0xFF1E293B)
                )
                Text("Estabelecendo conexão via satélite...", color = Color.Gray)
            }
        }
    }
}

@Composable
fun InfoCard(label: String, valor: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, color = Color(0xFF94A3B8), fontSize = 14.sp)
            Text(
                valor,
                color = Color(0xFF2D9CDB),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}