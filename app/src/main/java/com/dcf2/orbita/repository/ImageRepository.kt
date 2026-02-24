package com.dcf2.orbita.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.cloudinary.android.MediaManager
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

object ImageRepository {
    private var isInitialized = false

    // Inicializa a biblioteca (chamar no onCreate da Application ou MainActivity)
    fun init(context: Context) {
        if (!isInitialized) {
            val config = HashMap<String, String>()
            config["cloud_name"] = "dpd61csz2" // Coloque seu Cloud Name aqui
            config["secure"] = "true"
            MediaManager.init(context, config)
            isInitialized = true
        }
    }

    // Função Suspend (Corrotina) que transforma URI -> URL
    suspend fun uploadImage(uri: Uri, context: Context): String? {
        return suspendCancellableCoroutine { continuation ->
            try {
                // Necessário inicializar se ainda não foi
                init(context)

                val requestId = MediaManager.get().upload(uri)
                    .unsigned("orbita_preset") // <--- Coloque o nome do seu PRESET Unsigned aqui
                    .option("resource_type", "image")
                    .callback(object : com.cloudinary.android.callback.UploadCallback {
                        override fun onStart(requestId: String?) {
                            Log.d("Cloudinary", "Upload iniciado")
                        }

                        override fun onProgress(requestId: String?, bytes: Long, totalBytes: Long) {
                            // Opcional: Implementar progresso
                        }

                        override fun onSuccess(requestId: String?, resultData: Map<*, *>?) {
                            val url = resultData?.get("secure_url") as? String
                            Log.d("Cloudinary", "Sucesso! URL: $url")
                            continuation.resume(url)
                        }

                        override fun onError(requestId: String?, error: com.cloudinary.android.callback.ErrorInfo?) {
                            Log.e("Cloudinary", "Erro: ${error?.description}")
                            continuation.resume(null) // Retorna null em caso de erro
                        }

                        override fun onReschedule(requestId: String?, error: com.cloudinary.android.callback.ErrorInfo?) {
                            // Ignorar
                        }
                    })
                    .dispatch()

            } catch (e: Exception) {
                Log.e("Cloudinary", "Exception: ${e.message}")
                continuation.resume(null)
            }
        }
    }
}