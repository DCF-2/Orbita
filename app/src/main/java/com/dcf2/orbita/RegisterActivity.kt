package com.dcf2.orbita

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.btnRegisterFinal)
        btnRegister.setOnClickListener {
            // Simulação de cadastro
            Toast.makeText(this, "Conta criada! Faça login.", Toast.LENGTH_SHORT).show()

            // Volta para o Login
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}