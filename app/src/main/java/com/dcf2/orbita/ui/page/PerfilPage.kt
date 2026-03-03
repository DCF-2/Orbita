package com.dcf2.orbita.ui.page

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.dcf2.orbita.model.Usuario
import com.dcf2.orbita.viewmodel.MainViewModel

@Composable
fun PerfilPage(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel,
    navController: NavController,
    onLogout: () -> Unit
) {
    val user = viewModel.usuarioLogado
    var showSettingsDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Lançador nativo para escolher imagens da galeria
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            viewModel.atualizarFotoPerfil(uri, context)
        }
    }

    if (user == null) {
        Box(
            modifier = modifier.fillMaxSize().background(Color(0xFF050B14)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFFF2994A))
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050B14))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }
        }

        // Bloco do Avatar
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable {
                    // Abre a galeria ao clicar na foto
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
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
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Sem foto",
                    modifier = Modifier.size(80.dp),
                    tint = Color.White
                )
            }

            // Overlay semitransparente com ícone de câmara para indicar que é editável
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Alterar Foto",
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = user.nome.ifEmpty { "Explorador" }, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = user.email, color = Color(0xFF2D9CDB), fontSize = 16.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("Nível", user.nivel.toString())
            StatItem("XP", user.xp.toString())
            StatItem("Posts", viewModel.posts.filter { it.userId == user.id }.size.toString())
        }

        Spacer(modifier = Modifier.height(32.dp))

        Card(colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
            Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
                Text("Sobre Mim", color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(user.bio, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { showSettingsDialog = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E50)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Settings, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Editar Perfil")
        }

        Spacer(modifier = Modifier.height(12.dp))

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

    if (showSettingsDialog) {
        ConfiguracoesDialog(
            user = user,
            onDismiss = { showSettingsDialog = false },
            onSave = { novoNome, novaBio ->
                viewModel.atualizarPerfil(novoNome, novaBio)
                showSettingsDialog = false
            }
        )
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = Color(0xFFF2994A), fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(label, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun ConfiguracoesDialog(user: Usuario, onDismiss: () -> Unit, onSave: (String, String) -> Unit) {
    var nome by remember { mutableStateOf(user.nome) }
    var bio by remember { mutableStateOf(user.bio) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E293B),
        title = { Text("Editar Perfil", color = Color.White) },
        text = {
            Column {
                OutlinedTextField(
                    value = nome,
                    onValueChange = { nome = it },
                    label = { Text("Nome", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray,
                        focusedBorderColor = Color(0xFFF2994A)
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio", color = Color.Gray) },
                    modifier = Modifier.height(100.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray,
                        focusedBorderColor = Color(0xFFF2994A)
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onSave(nome, bio) }) {
                Text("Salvar", color = Color(0xFFF2994A), fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}