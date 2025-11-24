package com.dcf2.orbita

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dcf2.orbita.model.Observacao
class PostAdapter(private val listaPosts: List<Observacao>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPost: ImageView = itemView.findViewById(R.id.imgPost)
        val txtTitle: TextView = itemView.findViewById(R.id.txtPostTitle)
        val txtAuthor: TextView = itemView.findViewById(R.id.txtPostAuthor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = listaPosts[position]

        holder.txtTitle.text = post.titulo
        holder.txtAuthor.text = "por ${post.usuario.username} • ${post.data}"

        // Aqui definiríamos a imagem real. Por enquanto, usamos a cor de fundo do XML
        // holder.imgPost.setImageResource(post.fotoCeuResId)
    }

    override fun getItemCount(): Int = listaPosts.size
}