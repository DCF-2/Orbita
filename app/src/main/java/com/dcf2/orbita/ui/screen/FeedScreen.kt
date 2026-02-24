package com.dcf2.orbita.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dcf2.orbita.model.Post
import com.example.orbita.viewmodel.FeedViewModel


@Composable
fun FeedScreen(viewModel: FeedViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { /* A lógica de abrir o modal deve vir aqui ou ser passada por parâmetro */ }) {
                Icon(Icons.Default.Add, contentDescription = "Novo Post")
            }
        }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = Color(0xFFF2994A))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize() // Garante que ocupa a tela toda
                    .background(Color(0xFF050B14))
            ) {
                items(posts) { post ->
                    PostCard(post)
                }
            }
        }
    }
}

@Composable
fun PostCard(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // --- BLOCO NOVO: EXIBIÇÃO DA IMAGEM ---
            if (!post.fotoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = post.fotoUrl,
                    contentDescription = "Foto do post",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Altura fixa para ficar bonito
                        .clip(RoundedCornerShape(8.dp)), // Cantos arredondados
                    contentScale = ContentScale.Crop // Corta para preencher o espaço
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
            // --------------------------------------

            Text(text = post.titulo, style = MaterialTheme.typography.titleMedium, color = Color.White)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = post.descricao, style = MaterialTheme.typography.bodyMedium, color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(text = "Por: ${post.userName}", style = MaterialTheme.typography.labelSmall, color = Color(0xFFF2994A))
            }
        }
    }
}