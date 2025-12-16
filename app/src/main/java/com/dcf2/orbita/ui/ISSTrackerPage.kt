package com.dcf2.orbita.ui

import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun ISSTrackerPage() {
    Column(modifier = Modifier.fillMaxSize().background(Color(0xFF050B14))) {
        // 1. Área do Modelo 3D (WebView)
        Box(modifier = Modifier.height(300.dp).fillMaxWidth()) {
            AndroidView(factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    webViewClient = WebViewClient()
                    // URL oficial da NASA com modelo 3D interativo
                    loadUrl("https://solarsystem.nasa.gov/gltf_embed/2358")
                }
            })
        }

        // 2. Painel de Informações (API)
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Estação Espacial Internacional", style = MaterialTheme.typography.headlineSmall, color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))

            // Aqui virão os dados da API (Latitude, Longitude, Altitude)
            InfoCard("Latitude", "Carregando...")
            InfoCard("Longitude", "Carregando...")
            InfoCard("Velocidade", "27,600 km/h")
        }
    }
}

@Composable
fun InfoCard(label: String, valor: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = Color.Gray)
            Text(valor, color = Color(0xFF2D9CDB), style = MaterialTheme.typography.bodyLarge)
        }
    }
}