package com.dcf2.orbita.ui.page

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.dcf2.orbita.viewmodel.MainViewModel
import java.util.Locale

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ISSTrackerPage(viewModel: MainViewModel) {
    val issData = viewModel.issData // Pega os dados novos

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B14))
            .verticalScroll(scrollState)
    ) {
        // Modelo 3D (NASA)
        Box(
            modifier = Modifier
                .height(300.dp)
                .fillMaxWidth()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
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
                        setBackgroundColor(0x00000000)
                        webViewClient = WebViewClient()
                        loadUrl("https://solarsystem.nasa.gov/gltf_embed/2378")
                    }
                }
            )
        }

        // Painel de Dados
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Text(
                "Estação Espacial Internacional",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (issData != null) {
                // Indicador de Status (Dia ou Noite)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val statusColor = if (issData.visibility == "daylight") Color.Yellow else Color.Blue
                    val statusText = if (issData.visibility == "daylight") "À Luz do Sol" else "Na Sombra (Eclipse)"

                    Text("●", color = statusColor, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(statusText, color = statusColor, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Cards de Informação Rica
                InfoCard("Latitude", String.format(Locale.US, "%.4f°", issData.latitude))
                InfoCard("Longitude", String.format(Locale.US, "%.4f°", issData.longitude))
                InfoCard("Altitude", String.format(Locale.US, "%.1f km", issData.altitude))
                InfoCard("Velocidade", String.format(Locale.US, "%.0f km/h", issData.velocity))

                Spacer(modifier = Modifier.height(32.dp))
            } else {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    color = Color(0xFF2D9CDB),
                    trackColor = Color(0xFF1E293B)
                )
                Text("Conectando ao satélite...", color = Color.Gray)
            }
        }
    }
}

// O Composable InfoCard pode ser mantido igual ao anterior
@Composable
fun InfoCard(label: String, valor: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp)
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