package com.example.rs_p2

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class AdminDonationsActivity : AppCompatActivity() {

    private lateinit var donationRecyclerView: RecyclerView
    private lateinit var adapter: DonationAdapter
    private val donationList = mutableListOf<Donation>()
    private val proyectos = mutableMapOf<String, String>()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_donations)

        donationRecyclerView = findViewById(R.id.donationRecyclerView)
        donationRecyclerView.layoutManager = LinearLayoutManager(this)

        adapter = DonationAdapter(donationList, proyectos)
        donationRecyclerView.adapter = adapter

        getDonaciones()

        findViewById<Button>(R.id.backButton).setOnClickListener {
            finish() // Regresa a la pantalla anterior
        }
    }

    private fun getDonaciones() {
        db.collection("donaciones")
            .get()
            .addOnSuccessListener { result ->
                donationList.clear()
                val projectIds = mutableSetOf<String>()

                // Procesar todas las donaciones
                for (document in result) {
                    try {
                        // Deserializar el documento en un objeto Donation
                        val donation = document.toObject(Donation::class.java)

                        // Convertir 'montoDonado' de String a Double y luego de vuelta a String
                        donation.montoDonado =
                            ((document.getString("montoDonado") ?: "0.0").toDoubleOrNull() ?: 0.0).toString()

                        // Añadir el objeto Donation a la lista
                        donationList.add(donation)

                        // Añadir el ID del proyecto a la lista de IDs
                        projectIds.add(donation.idProyecto)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                // Obtener nombres de proyectos para todos los IDs recopilados
                getNombresDeProyectos(projectIds) {
                    // Actualizar el adaptador solo después de obtener todos los nombres
                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }

    private fun getNombresDeProyectos(projectIds: Set<String>, callback: (String?) -> Unit) {
        for (id in projectIds) {
            db.collection("proyectos").document(id)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nombre = document.getString("nombre")
                        callback(nombre)
                    } else {
                        callback("Proyecto no encontrado")
                    }
                }
                .addOnFailureListener { exception ->
                    exception.printStackTrace()
                    proyectos[id] = "Error en la solicitud"

                }
        }
    }



}
