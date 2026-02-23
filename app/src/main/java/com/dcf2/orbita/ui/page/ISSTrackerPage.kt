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

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ISSTrackerPage(viewModel: MainViewModel) { // <--- CORREÇÃO AQUI: Aceita MainViewModel
    val issResponse = viewModel.issResponse

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B14))
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .height(320.dp)
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
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true
                        setBackgroundColor(0x00000000)
                        webViewClient = WebViewClient()
                        loadUrl("https://solarsystem.nasa.gov/gltf_embed/2378")
                    }
                }
            )
        }

        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth()
        ) {
            Text(
                "Estação Espacial Internacional",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (issResponse != null) {
                val pos = issResponse.posicao

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("●", color = Color.Green, fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Dados em tempo real", color = Color.Green, fontSize = 14.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))

                val lat = pos.latitude.toDoubleOrNull() ?: 0.0
                val lon = pos.longitude.toDoubleOrNull() ?: 0.0

                InfoCard("Latitude", String.format("%.4f°", lat))
                InfoCard("Longitude", String.format("%.4f°", lon))

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