package com.example.rs_p2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.example.rs_p2.R

class Donar : AppCompatActivity() {

    // Variables para Firestore y la interfaz de usuario
    private lateinit var db: FirebaseFirestore
    private lateinit var projectNameTextView: TextView
    private lateinit var donationAmountEditText: EditText
    private lateinit var userNameEditText: EditText
    private lateinit var userEmailEditText: EditText
    private lateinit var acceptButton: Button
    private lateinit var exitButton: Button
    private lateinit var receiptCheckBox: CheckBox

    // Variables de control
    private var userMoney: Double = 0.0
    private var projectCurrentFunds: Double = 0.0
    private var userId: String? = null
    private var projectId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pan_donar)

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance()

        // Vincular los elementos de la interfaz con el código
        projectNameTextView = findViewById(R.id.project_name)
        donationAmountEditText = findViewById(R.id.donation_amount)
        userNameEditText = findViewById(R.id.user_name)
        userEmailEditText = findViewById(R.id.user_email)
        acceptButton = findViewById(R.id.accept_button)
        exitButton = findViewById(R.id.exit_button)
        receiptCheckBox = findViewById(R.id.want_receipt)

        // Obtener los extras del Intent
        projectId = intent.getStringExtra("PROJECT_ID")
        userId = intent.getStringExtra("USER_ID")

        if (projectId == null || userId == null) {
            Toast.makeText(this, "Error al obtener los datos necesarios", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Cargar detalles del proyecto y usuario
        loadProjectDetails(projectId!!)
        loadUserDetails(userId!!)

        // Configurar botón "Aceptar"
        acceptButton.setOnClickListener {
            handleDonation()
        }

        // Configurar botón "Salir"
        exitButton.setOnClickListener {
            finish()  // Cierra la actividad actual
        }
    }

    // Función para cargar los detalles del proyecto
    private fun loadProjectDetails(projectId: String) {
        val projectRef = db.collection("proyectos").document(projectId)

        projectRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val projectName = document.getString("nombre")
                    projectCurrentFunds = document.getDouble("montoRecaudado") ?: 0.0
                    projectNameTextView.text = projectName ?: "Sin nombre"
                } else {
                    Log.e("Donar", "El proyecto no existe")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Donar", "Error al obtener los detalles del proyecto: ", exception)
            }
    }

    // Función para cargar los detalles del usuario
    private fun loadUserDetails(userId: String) {
        val userRef = db.collection("usuarios").document(userId)

        userRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val userName = document.getString("nombre")
                    val userEmail = document.getString("email")
                    userMoney = (document.getString("dinero"))?.toDoubleOrNull()!!
                    userNameEditText.setText(userName ?: "Sin nombre")
                    userEmailEditText.setText(userEmail ?: "Sin correo")
                } else {
                    Log.e("Donar", "El usuario no existe")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Donar", "Error al obtener los detalles del usuario: ", exception)
            }
    }

    // Manejar la donación cuando el usuario presiona "Aceptar"
    private fun handleDonation() {
        val donationAmount = donationAmountEditText.text.toString().toDoubleOrNull()

        if (donationAmount == null || donationAmount <= 0) {
            Toast.makeText(this, "Por favor ingrese un monto válido", Toast.LENGTH_SHORT).show()
            return
        }

        if (donationAmount > userMoney) {
            Toast.makeText(this, "No tienes suficiente dinero para donar", Toast.LENGTH_SHORT).show()
            return
        }
        val porcentaje = donationAmount/userMoney
        if(porcentaje > 0.2){
            Toast.makeText(this, "Se está realizando una donación que supera el 20% de la cartera del usuario", Toast.LENGTH_SHORT).show()
            return
        }

        val newUserBalance = userMoney - donationAmount
        val newProjectFunds = projectCurrentFunds + donationAmount

        // Actualizar la base de datos
        updateUserBalance(newUserBalance.toString())
        updateProjectFunds(newProjectFunds)
        recordDonation(donationAmount)

        Toast.makeText(this, "Donación realizada con éxito", Toast.LENGTH_SHORT).show()
        finish()  // Cierra la actividad después de la donación
    }

    // Función para actualizar el saldo del usuario
    private fun updateUserBalance(newBalance: String) {
        val userRef = db.collection("usuarios").document(userId!!)
        userRef.update("dinero", newBalance)
            .addOnSuccessListener {
                Log.d("Donar", "Saldo de usuario actualizado")
            }
            .addOnFailureListener { e ->
                Log.e("Donar", "Error actualizando saldo del usuario", e)
            }
    }

    // Función para actualizar los fondos del proyecto
    private fun updateProjectFunds(newFunds: Double) {
        val projectRef = db.collection("proyectos").document(projectId!!)
        projectRef.update("montoRecaudado", newFunds)
            .addOnSuccessListener {
                Log.d("Donar", "Fondos del proyecto actualizados")
            }
            .addOnFailureListener { e ->
                Log.e("Donar", "Error actualizando fondos del proyecto", e)
            }
    }

    // Función para registrar la donación en Firestore
    private fun recordDonation(amount: Double) {
        val donationsRef = db.collection("donaciones")
        val donationData = hashMapOf(
            "idDonador" to userId,
            "idProyecto" to projectId,
            "montoDonado" to amount,
            "nombreDonador" to userNameEditText.text.toString(),
            "correoDonador" to userEmailEditText.text.toString(),
            "telefonoDonador" to "N/A"  // Si tienes el teléfono, lo puedes agregar aquí
        )

        donationsRef.add(donationData)
            .addOnSuccessListener {
                Log.d("Donar", "Donación registrada exitosamente")
            }
            .addOnFailureListener { e ->
                Log.e("Donar", "Error registrando donación", e)
            }
    }
}