package com.example.rs_p2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.example.rs_p2.R

class VProject : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore

    // Vistas para mostrar detalles del proyecto
    private lateinit var projectNameTextView: TextView
    private lateinit var projectDescriptionTextView: TextView
    private lateinit var projectCategoryTextView: TextView
    private lateinit var projectRecaudadoTextView: TextView
    private lateinit var projectFechaTextView: TextView
    private lateinit var projectObjetivoTextView: TextView

    // Botones
    private lateinit var donateButton: Button
    private lateinit var editButton: Button
    private lateinit var postCommentButton: Button
    private lateinit var backButton: Button

    // RecyclerView para comentarios
    private lateinit var commentsRecyclerView: RecyclerView
    private lateinit var commentsAdapter: CommentAdapterRecycler
    private val comments = mutableListOf<Map<String, Any>>()

    // Otros elementos para comentarios
    private lateinit var commentText: EditText
    private lateinit var commentRating: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detalles_project)

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance()

        // Vincular vistas de detalles del proyecto
        projectNameTextView = findViewById(R.id.project_name)
        projectDescriptionTextView = findViewById(R.id.project_description)
        projectCategoryTextView = findViewById(R.id.project_category)
        projectRecaudadoTextView = findViewById(R.id.project_recaudado)
        projectFechaTextView = findViewById(R.id.project_fecha)
        projectObjetivoTextView = findViewById(R.id.project_objetivo)

        donateButton = findViewById(R.id.donate_button)
        editButton = findViewById(R.id.edit_button)
        postCommentButton = findViewById(R.id.post_comment_button)
        backButton = findViewById(R.id.back_button)

        // Vincular vistas para la sección de comentarios
        commentsRecyclerView = findViewById(R.id.comments_recycler_view)
        commentsRecyclerView.layoutManager = LinearLayoutManager(this)
        commentsAdapter = CommentAdapterRecycler(comments)
        commentsRecyclerView.adapter = commentsAdapter

        commentText = findViewById(R.id.comment_text)
        commentRating = findViewById(R.id.comment_rating)

        // Obtener los extras del Intent
        val projectId = intent.getStringExtra("PROJECT_ID")
        val userId = intent.getStringExtra("USER_ID")
        val creatorId = intent.getStringExtra("CREATOR_ID")

        // Verificar datos y configurar botones
        if (projectId == null) {
            Log.e("VProject", "Error: projectId es nulo.")
            finish()
            return
        }

        if (userId != null && creatorId != null) {
            if (userId == creatorId) {
                editButton.visibility = Button.VISIBLE
            } else {
                donateButton.visibility = Button.VISIBLE
            }
        }

        // Configurar botón "Donar"
        donateButton.setOnClickListener {
            val intent = Intent(this, Donar::class.java).apply {
                putExtra("PROJECT_ID", projectId)
                putExtra("USER_ID", userId)
            }
            startActivity(intent)
        }

        editButton.setOnClickListener {
            val intent = Intent(this, EditProject::class.java).apply {
                putExtra("PROJECT_ID", projectId)
                putExtra("USER_ID", userId)
            }
            startActivity(intent)
        }

        // Configurar botón "Publicar comentario"
        postCommentButton.setOnClickListener {
            postComment(projectId, userId)
        }

        backButton.setOnClickListener {
            finish()  // Cierra la actividad actual
        }

        // Cargar detalles del proyecto y comentarios
        loadProjectDetails(projectId)
        loadComments(projectId)
    }

    private fun loadProjectDetails(projectId: String) {
        val projectRef = db.collection("proyectos").document(projectId)

        projectRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val nombre = document.getString("nombre")
                    val descripcion = document.getString("descripcion")
                    val categoria = document.getString("categoria")
                    val montoRecaudado = document.getDouble("montoRecaudado") ?: 0.0
                    val fechaLimite = document.getString("fechaLimite")
                    val objetivo = document.getDouble("objetivo") ?: 0.0

                    projectNameTextView.text = nombre ?: "Sin nombre"
                    projectDescriptionTextView.text = descripcion ?: "Sin descripción"
                    projectCategoryTextView.text = categoria ?: "Sin categoría"
                    projectRecaudadoTextView.text = montoRecaudado.toString()
                    projectFechaTextView.text = fechaLimite ?: "Sin fecha"
                    projectObjetivoTextView.text = objetivo.toString()
                } else {
                    Log.e("VProject", "El proyecto no existe o el documento no tiene datos.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("VProject", "Error al obtener los detalles del proyecto: ", exception)
            }
    }

    private fun loadComments(projectId: String) {
        db.collection("comentarios")
            .whereEqualTo("idProyecto", projectId)
            .get()
            .addOnSuccessListener { result ->
                comments.clear()
                for (document in result) {
                    comments.add(document.data)
                }
                commentsAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("VProject", "Error al cargar comentarios: ", exception)
            }
    }

    private fun postComment(projectId: String, userId: String?) {
        val text = commentText.text.toString()
        val rating = commentRating.selectedItem.toString().toIntOrNull() ?: 0

        if (userId != null && text.isNotEmpty()) {
            val comment = mapOf(
                "idUsuario" to userId,
                "idProyecto" to projectId,
                "texto" to text,
                "calificacion" to rating
            )

            db.collection("comentarios").add(comment)
                .addOnSuccessListener {
                    commentText.text.clear()
                    commentRating.setSelection(0)
                    loadComments(projectId)
                }
                .addOnFailureListener { e ->
                    Log.e("VProject", "Error al publicar comentario", e)
                }
        } else {
            Toast.makeText(this, "Error: el comentario o usuario es inválido", Toast.LENGTH_SHORT).show()
        }
    }
}
