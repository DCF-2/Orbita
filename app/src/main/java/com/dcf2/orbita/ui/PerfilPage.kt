package com.dcf2.orbita.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcf2.orbita.MainViewModel

@Composable
fun PerfilPage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    val user = viewModel.usuario

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050B14))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, null, modifier = Modifier.size(80.dp), tint = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(user.nome, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(user.handle, color = Color(0xFF2D9CDB), fontSize = 16.sp)

        Spacer(modifier = Modifier.height(24.dp))

        // Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("Nível", user.nivel.toString())
            StatItem("XP", user.xp.toString())
            StatItem("Posts", viewModel.observacoes.filter { it.autor == "Você" }.size.toString())
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Bio
        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text("Sobre Mim", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(user.bio, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E50)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Settings, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Configurações")
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color(0xFFF2994A), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.Gray, fontSize = 12.sp)
    }
}