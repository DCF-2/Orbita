package com.dcf2.orbita

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        setContent {
            RegisterScreen(
                onRegisterClick = { nome, email, senha -> cadastrarUsuario(nome, email, senha) },
                onBackClick = { finish() }
            )
        }
    }

    private fun cadastrarUsuario(nome: String, email: String, pass: String) {
        if (nome.isEmpty() || email.isEmpty() || pass.isEmpty()) return
        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                user?.updateProfile(UserProfileChangeRequest.Builder().setDisplayName(nome).build())?.addOnCompleteListener {
                    Toast.makeText(this, "Conta criada!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    finish()
                }
            } else {
                Toast.makeText(this, "Erro: ${task.exception?.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Composable
fun RegisterScreen(
    onRegisterClick: (String, String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var nome by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }

    // --- CORES & ESTILO ---
    val accentColor = Color(0xFFF2994A)
    val textColor = Color.White
    val containerColor = Color.White.copy(alpha = 0.1f)
    val fieldShape = RoundedCornerShape(16.dp)

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.bg_yourname),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Criar Conta", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = textColor)
            Spacer(modifier = Modifier.height(32.dp))

            @Composable
            fun OrbitaField(valStr: String, setVal: (String)->Unit, lbl: String, pass: Boolean = false) {
                OutlinedTextField(
                    value = valStr, onValueChange = setVal, label = { Text(lbl) },
                    visualTransformation = if (pass) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
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
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                )
            }

            OrbitaField(nome, { nome = it }, "Nome de exibição")
            OrbitaField(email, { email = it }, "E-mail")
            OrbitaField(senha, { senha = it }, "Senha (6 caracteres)", true)
            OrbitaField(confirmarSenha, { confirmarSenha = it }, "Confirmar Senha", true)

            Spacer(modifier = Modifier.height(24.dp))

            // Lógica de Validação para o Botão
            val tudoValido = nome.isNotEmpty() && email.contains("@") && senha.length >= 6 && senha == confirmarSenha

            Button(
                onClick = { onRegisterClick(nome, email, senha) },
                enabled = tudoValido, // Botão bloqueado se faltar dados
                shape = fieldShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor,
                    contentColor = Color.White,
                    disabledContainerColor = accentColor.copy(alpha = 0.3f),
                    disabledContentColor = Color.White.copy(alpha = 0.5f)
                ),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Cadastrar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Rodapé com Texto + Link
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Já tem uma conta? ",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Text(
                    text = "Fazer Login",
                    color = accentColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { onBackClick() }
                )
            }
        }
    }
}