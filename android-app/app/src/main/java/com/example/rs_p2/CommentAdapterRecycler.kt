package com.example.rs_p2

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class CommentAdapterRecycler(
    private val comments: List<Map<String, Any>>
) : RecyclerView.Adapter<CommentAdapterRecycler.CommentViewHolder>() {

    private val db = FirebaseFirestore.getInstance()
    private val userNameCache = mutableMapOf<String, String>()

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val commentUser: TextView = itemView.findViewById(R.id.comment_user)
        val commentText: TextView = itemView.findViewById(R.id.comment_text)
        val commentRating: RatingBar = itemView.findViewById(R.id.comment_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_comment, parent, false)
        return CommentViewHolder(itemView)
    }

    override fun getItemCount(): Int = comments.size

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        val currentComment = comments[position]

        holder.commentText.text = currentComment["texto"]?.toString() ?: "Comentario sin texto"
        val ratingValue = currentComment["calificacion"]?.toString()?.toFloatOrNull() ?: 0f
        holder.commentRating.rating = ratingValue

        val userId = currentComment["idUsuario"]?.toString()

        if (userId != null) {
            if (userNameCache.containsKey(userId)) {
                holder.commentUser.text = userNameCache[userId]
            } else {
                db.collection("usuarios").document(userId).get()
                    .addOnSuccessListener { document ->
                        val userName = document.getString("nombre") ?: "Usuario desconocido"
                        userNameCache[userId] = userName
                        holder.commentUser.text = userName
                    }
                    .addOnFailureListener { e ->
                        Log.e("CommentAdapterRecycler", "Error al obtener el nombre de usuario", e)
                        holder.commentUser.text = "Usuario desconocido"
                    }
            }
        } else {
            holder.commentUser.text = "Usuario desconocido"
        }
    }
}
