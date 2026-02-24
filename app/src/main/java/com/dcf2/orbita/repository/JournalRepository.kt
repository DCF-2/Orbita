package com.example.orbita.repository

import com.dcf2.orbita.model.Post
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class JournalRepository {
    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("posts")

    // Função para buscar posts (Feed)
    suspend fun getPosts(): List<Post> {
        return try {
            val snapshot = postsCollection
                .orderBy("dataObservacao", Query.Direction.DESCENDING)
                .get()
                .await()

            snapshot.documents.mapNotNull { doc ->
                doc.toObject(Post::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList() // Em produção, trate o erro melhor
        }
    }

    // Função para criar um novo post
    suspend fun addPost(post: Post): Boolean {
        return try {
            postsCollection.add(post).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}