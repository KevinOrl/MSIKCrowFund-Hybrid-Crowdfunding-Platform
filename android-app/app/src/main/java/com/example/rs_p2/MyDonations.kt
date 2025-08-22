package com.example.rs_p2

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore

class MyDonations : AppCompatActivity(){
    // Instancia de Firebase Firestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_donations)

        //Recibe el parametro que contiene el id del proyecto a editar
        val idUsuario = intent.getStringExtra("idUsuario").toString()

        //Crea la tabla para añadir el resultado de las donaciones
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewDonaciones)
        recyclerView.layoutManager = LinearLayoutManager(this)

        //Función con callback para obtener el nombre del proyecto buscado por el idProyecto
        fun obtenerNombreProyecto(idProyecto: String, callback: (String?) -> Unit) {
            db.collection("proyectos").document(idProyecto).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nombre = document.getString("nombre")
                        callback(nombre)
                    } else {
                        callback("Proyecto no encontrado")
                    }
                }
                .addOnFailureListener {
                    callback(null)
                }
        }

        // Query para traer las donaciones del usuario
        db.collection("donaciones").whereEqualTo("idDonador", idUsuario).get()
            .addOnSuccessListener { documents ->
                val donaciones = mutableListOf<Donacion>()
                for (document in documents) {
                    val idProyecto = document.getString("idProyecto").toString()
                    // Verificar si montoDonado es un string y obtenerlo de manera segura
                    val monto = if (document.contains("montoDonado") && document.get("montoDonado") is String) {
                        document.getString("montoDonado") ?: "0.0"
                    } else {
                        "0.0"
                    }

                    obtenerNombreProyecto(idProyecto) { nombreProyecto ->
                        if (nombreProyecto != null) {
                            donaciones.add(Donacion(nombreProyecto, monto))
                            val adapter = DonacionAdaptador(donaciones)
                            recyclerView.adapter = adapter
                        } else {
                            Toast.makeText(this, "Error al obtener el nombre del proyecto", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Error al obtener las donaciones: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }



        //Se devuelve a la ventana anterior
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish()
        }
    }
}
