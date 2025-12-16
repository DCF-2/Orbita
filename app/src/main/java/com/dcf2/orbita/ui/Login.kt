package com.dcf2.orbita.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcf2.orbita.R

@Composable
fun LoginScreen(
    onLoginClick: (String, String) -> Unit,
    onGoogleClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    // --- CORES & ESTILO ---
    val accentColor = Color(0xFFF2994A) // Laranja do Projeto
    val textColor = Color.White
    val containerColor = Color.White.copy(alpha = 0.1f) // Vidro
    val fieldShape = RoundedCornerShape(16.dp)

    Box(modifier = Modifier.fillMaxSize()) {
        // 1. Wallpaper
        Image(
            painter = painterResource(id = R.drawable.bg_space),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // 2. Conteúdo Centralizado
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Órbita",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Campo Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                shape = fieldShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedLabelColor = accentColor,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = accentColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Senha
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Senha") },
                visualTransformation = PasswordVisualTransformation(),
                shape = fieldShape,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = containerColor,
                    unfocusedContainerColor = containerColor,
                    focusedBorderColor = accentColor,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.5f),
                    focusedLabelColor = accentColor,
                    unfocusedLabelColor = Color.LightGray,
                    cursorColor = accentColor,
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Botão Entrar (Com lógica de Habilitado/Desabilitado)
            val camposPreenchidos = email.isNotEmpty() && password.isNotEmpty()

            Button(
                onClick = { onLoginClick(email, password) },
                enabled = camposPreenchidos, // Só ativa se preencher tudo
                shape = fieldShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = Color.White,
                    disabledContainerColor = accentColor.copy(alpha = 0.3f), // Fica apagadinho se desabilitado
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Entrar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Seção Google Minimalista ---
            Text("ou entre com", color = Color.Cyan, fontSize = 14.sp)

            Spacer(modifier = Modifier.height(16.dp))

            // Apenas o Ícone Redondo (Botão clicável)
            IconButton(
                onClick = { onGoogleClick() },
                modifier = Modifier
                    .size(56.dp) // Tamanho do ícone
                    .clip(CircleShape)
                    .padding(4.dp) // Margem para o clique
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google), // Sua imagem 'google'
                    contentDescription = "Login com Google",
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(48.dp))

            // --- Rodapé: Texto + Link Colorido ---
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Não tem uma conta? ",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = "Criar conta",
                    color = accentColor, // Cor de destaque (Laranja/Azul)
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onRegisterClick() }
                )
            }
        }
    }
}