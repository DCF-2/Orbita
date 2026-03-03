package com.dcf2.orbita.repository

import android.util.Log
import com.dcf2.orbita.model.Usuario
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val db = FirebaseFirestore.getInstance()
    private val usersCollection = db.collection("users")

    // Salva ou atualiza um usuário no Firestore
    suspend fun saveUser(usuario: Usuario): Boolean {
        return try {
            usersCollection.document(usuario.id).set(usuario).await()
            Log.d("UserRepository", "Usuário salvo com sucesso!")
            true
        } catch (e: Exception) {
            Log.e("UserRepository", "Erro ao salvar usuário: ${e.message}")
            false
        }
    }

    // Busca os dados do usuário pelo ID
    suspend fun getUser(userId: String): Usuario? {
        return try {
            val document = usersCollection.document(userId).get().await()
            if (document.exists()) {
                document.toObject(Usuario::class.java)
            } else {
                null // Usuário ainda não existe no banco
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Erro ao buscar usuário: ${e.message}")
            null
        }
    }

    // Função que usaremos na Issue #8 para o Mapa
    suspend fun updateUserLocation(userId: String, lat: Double, lng: Double) {
        try {
            usersCollection.document(userId).update(
                mapOf("latitude" to lat, "longitude" to lng)
            ).await()
        } catch (e: Exception) {
            Log.e("UserRepository", "Erro ao atualizar localização: ${e.message}")
        }
    }
}