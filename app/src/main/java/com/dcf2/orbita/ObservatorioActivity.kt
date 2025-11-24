package com.dcf2.orbita

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class ObservatorioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_observatorio)

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_observatorio // Marca o ícone certo

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // Fecha essa tela para não empilhar
                    true
                }
                R.id.nav_observatorio -> true // Já estamos aqui
                R.id.nav_perfil -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}