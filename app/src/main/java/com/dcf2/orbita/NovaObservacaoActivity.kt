package com.dcf2.orbita

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class NovaObservacaoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nova_observacao)

        val inputTitulo = findViewById<EditText>(R.id.inputTitulo)
        val inputDesc = findViewById<EditText>(R.id.inputDescricao)
        val btnSalvar = findViewById<Button>(R.id.btnSalvarPost)

        btnSalvar.setOnClickListener {
            val titulo = inputTitulo.text.toString()
            val desc = inputDesc.text.toString()

            if (titulo.isNotEmpty()) {
                // 1. Salva na lista fake
                MockData.adicionarPost(titulo, desc)

                // 2. Avisa o usuário
                Toast.makeText(this, "Observação Registrada!", Toast.LENGTH_SHORT).show()

                // 3. Fecha a tela e volta para a Home
                finish()
            } else {
                Toast.makeText(this, "Preencha o título!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}