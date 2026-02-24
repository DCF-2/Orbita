package com.dcf2.orbita.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dcf2.orbita.viewmodel.MainViewModel
import com.dcf2.orbita.model.Observacao

@Composable
fun HomePage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF050B14)) // Fundo espacial
            .padding(16.dp)
    ) {
        Text(
            text = "Feed Órbita",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(viewModel.posts) { obs ->
                PostItem(obs)
            }
        }
    }
}

@Composable
fun PostItem(obs: Observacao) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = obs.titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = obs.descricao, color = Color(0xFF94A3B8), fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "por ${obs.autor} em ${obs.data}", color = Color(0xFFF2994A), fontSize = 12.sp)
        }
    }
}