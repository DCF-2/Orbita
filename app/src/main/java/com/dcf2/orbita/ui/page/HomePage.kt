package com.dcf2.orbita.ui.page

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Repeat
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dcf2.orbita.model.Post
import com.dcf2.orbita.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    // Controla qual post está com a gaveta de comentários aberta
    var postParaComentar by remember { mutableStateOf<Post?>(null) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    Box(modifier = modifier.fillMaxSize().background(Color(0xFF050B14))) {
        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Feed Órbita",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(viewModel.posts) { post ->
                    InstagramStylePostItem(
                        post = post,
                        viewModel = viewModel,
                        onCommentClick = { postParaComentar = post } // Abre o BottomSheet
                    )
                }
            }
        }

        // Gaveta de Comentários (BottomSheet)
        if (postParaComentar != null) {
            ModalBottomSheet(
                onDismissRequest = { postParaComentar = null },
                sheetState = sheetState,
                containerColor = Color(0xFF121A28), // Cor de fundo do bottom sheet
                dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
            ) {
                ComentariosSheet(post = postParaComentar!!)
            }
        }
    }
}

@Composable
fun InstagramStylePostItem(
    post: Post,
    viewModel: MainViewModel,
    onCommentClick: () -> Unit
) {
    val context = LocalContext.current
    var isLiked by remember { mutableStateOf(false) }
    var likeCount by remember { mutableStateOf(post.likes) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF121A28)),
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // CABEÇALHO
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                if (post.userAvatar.isNotEmpty()) {
                    AsyncImage(
                        model = post.userAvatar,
                        contentDescription = "Avatar",
                        modifier = Modifier.size(36.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Avatar padrão",
                        tint = Color.Gray,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = post.userName.ifEmpty { "Astrônomo Anônimo" },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    val sdf = SimpleDateFormat("dd MMM, HH:mm", Locale.getDefault())
                    val dataFormatada = sdf.format(post.dataObservacao.toDate())
                    Text(text = dataFormatada, color = Color.Gray, fontSize = 11.sp)
                }
            }

            // IMAGEM
            if (!post.fotoUrl.isNullOrEmpty()) {
                AsyncImage(
                    model = post.fotoUrl,
                    contentDescription = "Imagem",
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f).background(Color.Black),
                    contentScale = ContentScale.Crop
                )
            }

            // BARRA DE AÇÕES
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp)
            ) {
                // Like
                IconButton(onClick = {
                    isLiked = !isLiked
                    likeCount += if (isLiked) 1 else -1
                }) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Gostar",
                        tint = if (isLiked) Color.Red else Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Comentário
                IconButton(onClick = onCommentClick) {
                    Icon(
                        imageVector = Icons.Outlined.ChatBubbleOutline,
                        contentDescription = "Comentar",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Repost
                IconButton(onClick = {
                    Toast.makeText(context, "Repostado na tua órbita!", Toast.LENGTH_SHORT).show()
                }) {
                    Icon(
                        imageVector = Icons.Outlined.Repeat,
                        contentDescription = "Repostar",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f)) // Empurra o partilhar para a direita

                // Partilha (Copiar Link / Enviar)
                IconButton(onClick = { partilharPost(context, post) }) {
                    Icon(
                        imageVector = Icons.Outlined.Send,
                        contentDescription = "Partilhar",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            // LIKES & TEXTO
            if (likeCount > 0) {
                Text(
                    text = "$likeCount curtidas",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }

            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)) {
                Row {
                    Text(
                        text = post.userName.ifEmpty { "Usuário" },
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = post.titulo,
                        color = Color.White,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }

                if (post.descricao.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = post.descricao,
                        color = Color(0xFFCCCCCC),
                        fontSize = 14.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Texto clicável para abrir comentários
                Text(
                    text = "Ver todos os comentários",
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .clickable { onCommentClick() }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComentariosSheet(post: Post) {
    var novoComentario by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.7f) // Ocupa 70% da tela
            .padding(16.dp)
    ) {
        Text(
            text = "Comentários",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.DarkGray)

        // Lista de Comentários (Aqui usaríamos um LazyColumn com comentários do Firebase no futuro)
        Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("Seja o primeiro a comentar nesta observação!", color = Color.Gray)
        }

        // Campo para escrever comentário
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = novoComentario,
                onValueChange = { novoComentario = it },
                placeholder = { Text("Adicionar um comentário...", color = Color.Gray) },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFF2994A),
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.LightGray
                )
            )
            IconButton(
                onClick = { /* TODO: Enviar comentário para o Firebase */ novoComentario = "" },
                enabled = novoComentario.isNotBlank()
            ) {
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = "Enviar",
                    tint = if (novoComentario.isNotBlank()) Color(0xFFF2994A) else Color.Gray
                )
            }
        }
        // Espaço para não ficar colado no teclado
        Spacer(modifier = Modifier.height(32.dp))
    }
}

fun partilharPost(context: Context, post: Post) {
    val textoPartilha = """
        🚀 Olha esta observação no Órbita!
        
        *${post.titulo}*
        ${post.descricao}
        
        Descarrega o app Órbita para veres as estrelas.
    """.trimIndent()

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Observação Espacial - Órbita")
        putExtra(Intent.EXTRA_TEXT, textoPartilha)
    }

    val chooser = Intent.createChooser(intent, "Partilhar com...")
    context.startActivity(chooser)
}