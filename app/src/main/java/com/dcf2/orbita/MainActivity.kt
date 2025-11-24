package com.dcf2.orbita

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.dcf2.orbita.R

class MainActivity : AppCompatActivity() {

    // Declaramos estas variáveis aqui em cima para podermos usá-las no onResume depois
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // --- 1. Configuração da Lista (Feed) ---
        recyclerView = findViewById(R.id.listaOrbita)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Carrega a lista inicial de posts falsos
        adapter = PostAdapter(MockData.posts)
        recyclerView.adapter = adapter

        // --- 2. Configuração do Botão Flutuante (Novo Post) ---
        val fab = findViewById<FloatingActionButton>(R.id.fabNewPost)
        fab.setOnClickListener {
            // Abre a tela de criar nova observação
            val intent = Intent(this, NovaObservacaoActivity::class.java)
            startActivity(intent)
        }

        // --- 3. Configuração da Navegação Inferior ---
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.nav_home

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> true // Já estamos na Home
                R.id.nav_observatorio -> {
                    startActivity(Intent(this, ObservatorioActivity::class.java))
                    finish()
                    true
                }
                R.id.nav_perfil -> {
                    startActivity(Intent(this, PerfilActivity::class.java))
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    // Este método é chamado automaticamente pelo Android sempre que a tela volta a aparecer
    // (Por exemplo, quando você fecha a tela de "Nova Observação" e volta pra cá)
    override fun onResume() {
        super.onResume()
        // Recarrega a lista para mostrar o novo post que acabamos de criar
        adapter = PostAdapter(MockData.posts)
        recyclerView.adapter = adapter
    }
}