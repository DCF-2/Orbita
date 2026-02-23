package com.dcf2.orbita.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcf2.orbita.viewmodel.MainViewModel

@Composable
fun DiarioPage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    // Filtra apenas posts do usuário "Você" (simulação)
    val meusPosts = viewModel.observacoes.filter { it.autor == "Você" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050B14))
            .padding(16.dp)
    ) {
        Text(text = "Meu Diário", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(meusPosts) { obs ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Color(0xFF1E293B), shape = RoundedCornerShape(8.dp))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = obs.titulo, color = Color.White, fontWeight = FontWeight.Bold)
                        Text(text = obs.descricao, color = Color.Gray)
                    }
                    IconButton(onClick = { viewModel.removeObservacao(obs) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Excluir", tint = Color.Red)
                    }
                }
            }
        }
    }
}