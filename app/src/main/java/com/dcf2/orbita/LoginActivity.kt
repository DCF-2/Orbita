package com.dcf2.orbita

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF050B14)), // Fundo Dark
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Orbita", fontSize = 32.sp, color = Color.White)
                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF2994A))
                    ) {
                        Text("Entrar (Simulado)")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(onClick = {
                        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                    }) {
                        Text("Criar Conta", color = Color(0xFF2D9CDB))
                    }
                }
            }
        }
    }
}