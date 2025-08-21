package com.example.rs_p2

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore

class CommentAdapter(
    context: Context,
    private val comments: List<Map<String, Any>>
) : ArrayAdapter<Map<String, Any>>(context, 0, comments) {

    private val db = FirebaseFirestore.getInstance()
    private val userNameCache = mutableMapOf<String, String>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val itemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)

        val currentComment = comments[position]

        // Obtener y establecer el texto del comentario
        val commentText = itemView.findViewById<TextView>(R.id.comment_text)
        commentText.text = currentComment["texto"]?.toString() ?: "Comentario sin texto"

        // Obtener y establecer la calificación
        val commentRating = itemView.findViewById<RatingBar>(R.id.comment_rating)
        val ratingValue = currentComment["calificacion"]?.toString()?.toFloatOrNull() ?: 0f
        commentRating.rating = ratingValue

        // Obtener y establecer el nombre del usuario
        val commentUser = itemView.findViewById<TextView>(R.id.comment_user)
        val userId = currentComment["idUsuario"]?.toString()

        if (userId != null) {
            if (userNameCache.containsKey(userId)) {
                // Si el nombre de usuario está en la caché, úsalo
                commentUser.text = userNameCache[userId]
            } else {
                // Si no está, obtén el nombre de usuario de Firestore
                db.collection("usuarios").document(userId).get()
                    .addOnSuccessListener { document ->
                        if (document != null && document.exists()) {
                            val userName = document.getString("nombre") ?: "Usuario desconocido"
                            userNameCache[userId] = userName
                            commentUser.text = userName
                        } else {
                            commentUser.text = "Usuario desconocido"
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("CommentAdapter", "Error al obtener el nombre de usuario", e)
                        commentUser.text = "Usuario desconocido"
                    }
            }
        } else {
            commentUser.text = "Usuario desconocido"
        }

        return itemView
    }
}
