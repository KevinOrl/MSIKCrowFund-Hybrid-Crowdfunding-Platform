package com.example.rs_p2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView
import com.google.firebase.firestore.FirebaseFirestore

class EstadisticasActivity : AppCompatActivity() {

    private lateinit var proyectosCountTextView: TextView
    private lateinit var donacionesCountTextView: TextView
    private lateinit var usuariosCountTextView: TextView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estadisticas)

        proyectosCountTextView = findViewById(R.id.proyectosCount)
        donacionesCountTextView = findViewById(R.id.donacionesCount)
        usuariosCountTextView = findViewById(R.id.usuariosCount)
        backButton = findViewById(R.id.backButton)

        // Inicializa Firestore
        val db = FirebaseFirestore.getInstance()

        // Obtiene y muestra la cantidad de usuarios activos
        db.collection("usuarios")
            .whereEqualTo("activo", true)
            .get()
            .addOnSuccessListener { snapshot ->
                usuariosCountTextView.text = snapshot.size().toString()
            }

        // Obtiene y muestra la cantidad de proyectos
        db.collection("proyectos")
            .get()
            .addOnSuccessListener { snapshot ->
                proyectosCountTextView.text = snapshot.size().toString()
            }

        // Obtiene y muestra la cantidad de donaciones
        db.collection("donaciones")
            .get()
            .addOnSuccessListener { snapshot ->
                donacionesCountTextView.text = snapshot.size().toString()
            }

        // Configura el botón de regresar
        backButton.setOnClickListener {
            finish() // Vuelve a la actividad anterior
        }
    }
}
