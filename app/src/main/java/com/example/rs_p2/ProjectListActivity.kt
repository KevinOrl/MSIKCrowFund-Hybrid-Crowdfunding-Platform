package com.example.rs_p2

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class ProjectListActivity : AppCompatActivity() {

    private lateinit var projectRecyclerView: RecyclerView
    private lateinit var projectsAdapter: ProjectsAdapter
    private var projectList = mutableListOf<AProject>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_list)

        projectRecyclerView = findViewById(R.id.projectRecyclerView)
        projectRecyclerView.layoutManager = LinearLayoutManager(this)
        projectsAdapter = ProjectsAdapter(projectList)
        projectRecyclerView.adapter = projectsAdapter

        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            finish() // Regresa a la pantalla anterior
        }

        fetchProjectsFromFirestore()
    }

    private fun fetchProjectsFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("proyectos").get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Crear el objeto Project manualmente
                    val project = AProject(
                        id = document.id,
                        nombre = document.getString("nombre") ?: "",
                        categoria = document.getString("categoria") ?: "",
                        descripcion = document.getString("descripcion") ?: "",
                        montoRecaudado = document.getDouble("montoRecaudado") ?: 0.0,
                        objetivo = document.get("objetivo").toString(), // Convertir a String de forma segura
                        fechaLimite = document.getString("fechaLimite") ?: ""
                    )
                    projectList.add(project)
                }
                projectsAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Manejar error de carga de datos
            }
    }

}
