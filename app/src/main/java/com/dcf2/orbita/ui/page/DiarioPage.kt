package com.dcf2.orbita.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage // Certifique-se de ter a lib do Coil
import com.dcf2.orbita.viewmodel.MainViewModel
import com.dcf2.orbita.model.Post

@Composable
fun DiarioPage(viewModel: MainViewModel) {
    // Agora observamos a lista de 'posts' (que tem suporte a imagem)
    val posts = viewModel.posts
    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showDialog = true }, // A lógica do Dialog está na MainActivity ou aqui?
                // Se o Dialog for chamado na MainActivity, este botão deve avisar a MainActivity via callback.
                // Mas se você quiser simplificar, pode deixar o botão aqui apenas se o Dialog estiver aqui.
                // Vou assumir que o FAB aqui é apenas visual ou aciona o estado que abre o dialog.
                containerColor = Color(0xFFF2994A)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Novo")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFF050B14))
                .padding(16.dp)
        ) {
            item {
                Text(
                    "Meu Diário",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            items(posts) { post ->
                PostItem(post)
            }
        }
    }
}

@Composable
fun PostItem(post: Post) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Se tiver imagem, mostra
            if (!post.fotoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = post.fotoUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = post.titulo,
                style = MaterialTheme.typography.titleLarge,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = post.descricao,
                style = MaterialTheme.typography.bodyMedium,
                color = Color(0xFFB0BEC5)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Por: ${post.userName}",
                style = MaterialTheme.typography.labelSmall,
                color = Color(0xFFF2994A)
            )
        }
    }
}