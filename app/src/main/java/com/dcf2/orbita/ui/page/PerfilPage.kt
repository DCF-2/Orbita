package com.dcf2.orbita.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dcf2.orbita.viewmodel.MainViewModel

@Composable
fun PerfilPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    onLogout: () -> Unit
) {
    // 1. Usa o utilizador real vindo do Firebase
    val user = viewModel.usuarioLogado

    // 2. Enquanto o Firebase não devolve os dados, mostra um "Loading"
    if (user == null) {
        Box(
            modifier = modifier.fillMaxSize().background(Color(0xFF050B14)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFF2994A))
        }
        return // Impede que o resto do ecrã seja desenhado sem dados
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050B14))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar (Com validação se a pessoa não tiver foto)
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray),
            contentAlignment = Alignment.Center
        ) {
            if (user.avatarUrl.isNotEmpty()) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Fallback para quem regista por Email/Senha sem foto
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Sem foto",
                    modifier = Modifier.size(80.dp),
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = user.nome.ifEmpty { "Explorador" }, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        // Substituímos o "@handle" pelo email da pessoa, já que é o que temos do Firebase por agora
        Text(text = user.email, color = Color(0xFF2D9CDB), fontSize = 16.sp)

        Spacer(modifier = Modifier.height(24.dp))

        // Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("Nível", user.nivel.toString())
            StatItem("XP", user.xp.toString())
            // Conta os posts filtrando pelo ID real do utilizador, não pela string "Você"
            StatItem("Posts", viewModel.posts.filter { it.userId == user.id }.size.toString())
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

        // Botão Configurações
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E50)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Settings, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Configurações")
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botão de Logout
        Button(
            onClick = { onLogout() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC0392B)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.ExitToApp, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Sair da Conta")
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