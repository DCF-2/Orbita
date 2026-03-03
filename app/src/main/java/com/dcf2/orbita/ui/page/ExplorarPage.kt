package com.dcf2.orbita.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dcf2.orbita.viewmodel.MainViewModel
import com.dcf2.orbita.model.Curiosidade
import com.dcf2.orbita.model.EventoAstronomico
import java.util.Locale

@Composable
fun ExplorarPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050B14))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Seção ISS Tracker
        item {
            ISSTrackerCard(
                viewModel = viewModel,
                onClick = { navController.navigate("iss_detalhes") }
            )
        }

        // Seção Eventos
        item {
            Text("📅 Eventos Próximos", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(viewModel.eventos) { evento ->
                    EventoCard(evento)
                }
            }
        }

        // Seção Curiosidades
        item {
            Text("🌌 Curiosidades Cósmicas", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        items(viewModel.curiosidades) { curiosidade ->
            CuriosidadeItem(curiosidade)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ISSTrackerCard(viewModel: MainViewModel, onClick: () -> Unit) {
    val issData = viewModel.issData
    val user = viewModel.usuarioLogado

    // Estado para guardar o texto de distância
    var distanciaText by remember { mutableStateOf("Calculando distância...") }

    // Calcula a distância sempre que a ISS ou o Usuário mudarem de posição
    LaunchedEffect(issData, user) {
        val userLat = user?.latitude
        val userLng = user?.longitude
        val issLat = issData?.latitude
        val issLng = issData?.longitude

        if (userLat != null && userLng != null && issLat != null && issLng != null) {
            val results = FloatArray(1)
            android.location.Location.distanceBetween(userLat, userLng, issLat, issLng, results)
            val distKm = results[0] / 1000 // Converter metros para Km
            distanciaText = "A ${String.format(Locale.US, "%.0f", distKm)} km de você"
        } else if (issData != null) {
            distanciaText = "Velocidade: ${String.format(Locale.US, "%.0f", issData.velocity)} km/h"
        }
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.RocketLaunch, contentDescription = null, tint = Color(0xFF2D9CDB), modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Onde está a ISS?", color = Color.White, fontWeight = FontWeight.Bold)
                Text("Toque para rastrear ao vivo", color = Color.Gray, fontSize = 14.sp)
                // O texto agora é dinâmico com a distância!
                Text(distanciaText, color = Color(0xFFF2994A), fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun EventoCard(evento: EventoAstronomico) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C3E50)),
        modifier = Modifier.width(250.dp).height(140.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.DateRange, null, tint = Color(0xFFF2C94C))
                Spacer(modifier = Modifier.width(8.dp))
                Text(evento.data, color = Color(0xFFF2C94C), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(evento.titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(evento.descricao, color = Color.LightGray, fontSize = 12.sp, maxLines = 2)
        }
    }
}

@Composable
fun CuriosidadeItem(curiosidade: Curiosidade) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E293B), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .background(curiosidade.corCartao, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(curiosidade.categoria.take(1), color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(curiosidade.titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(curiosidade.subtitulo, color = Color.Gray, fontSize = 14.sp)
        }
    }
}