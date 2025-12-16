package com.dcf2.orbita

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.dcf2.orbita.ui.LoginScreen
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    // Configuração do Resultado do Google
    private val launcherGoogle = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val conta = task.getResult(ApiException::class.java)
            // Se deu certo no Google, autentica no Firebase
            firebaseAuthWithGoogle(conta.idToken!!)
        } catch (e: ApiException) {
            Toast.makeText(this, "Erro Google: ${e.statusCode}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        auth = FirebaseAuth.getInstance()

        setContent {
            // Chamamos a tela visual e passamos as funções de lógica
            LoginScreen(
                onLoginClick = { email, senha ->
                    fazerLoginEmail(email, senha)
                },
                onGoogleClick = {
                    iniciarLoginGoogle()
                },
                onRegisterClick = {
                    // Navegar para tela de Registro (precisa criar essa Activity depois)
                    startActivity(Intent(this, RegisterActivity::class.java))
                }
            )
        }
    }

    // --- Verifica se já está logado ao abrir o app ---
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            irParaMain()
        }
    }

    // --- MÉTODOS DE LOGIN ---

    // 1. Login com Email e Senha (Do seu exemplo WeatherApp)
    private fun fazerLoginEmail(email: String, pass: String) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show()
                    irParaMain()
                } else {
                    Toast.makeText(this, "Falha: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // 2. Iniciar fluxo do Google
    private fun iniciarLoginGoogle() {
        val clientId = "185785159810-g1ijhfclaps0r4mg9mrhiojpimtmgmbi.apps.googleusercontent.com"
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientId)
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, gso)
        launcherGoogle.launch(googleClient.signInIntent)
    }

    // 3. Autenticar Google no Firebase
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    irParaMain()
                } else {
                    Toast.makeText(this, "Erro no Firebase Google", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun irParaMain() {
        // Flags para limpar o histórico e o usuário não voltar pro login com o botão "voltar"
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}