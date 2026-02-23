package com.dcf2.orbita.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.orbita.viewmodel.ExplorarViewModel

@Composable
fun ExplorarScreen(viewModel: ExplorarViewModel = viewModel()) {
    val apod by viewModel.apod.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF050B14)) // Fundo Dark Space
            .verticalScroll(scrollState)
    ) {
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize().height(400.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFF2994A))
            }
        } else {
            apod?.let { dados ->
                // Imagem da NASA
                AsyncImage(
                    model = dados.imagemUrl,
                    contentDescription = "Imagem Astronômica do Dia",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    contentScale = ContentScale.Crop
                )

                // Textos Explicativos
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = dados.titulo,
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = dados.data,
                        style = MaterialTheme.typography.labelMedium,
                        color = Color(0xFFF2994A)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = dados.explicacao,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.LightGray
                    )
                }
            } ?: run {
                // Estado de erro simples
                Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                    Text("Falha ao conectar com a NASA.", color = Color.Red)
                }
            }
        }
    }
}