package com.example.rs_p2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class EditProfile : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    private lateinit var nombreField: EditText
    private lateinit var cedulaField: EditText
    private lateinit var emailField: EditText
    private lateinit var telefonoField: EditText
    private lateinit var areaDeTrabajoField: EditText
    private lateinit var dineroField: EditText

    private lateinit var saveButton: Button
    private lateinit var backButton: Button

    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_edit)

        // Referencias a los elementos de la interfaz
        nombreField = findViewById(R.id.editName)
        cedulaField = findViewById(R.id.editCedula)
        emailField = findViewById(R.id.editEmail)
        telefonoField = findViewById(R.id.editTelefono)
        areaDeTrabajoField = findViewById(R.id.editAreaTrabajo)
        dineroField = findViewById(R.id.editDinero)

        saveButton = findViewById(R.id.saveButton)
        backButton = findViewById(R.id.backButton)

        // Obtener el userId del Intent
        userId = intent.getStringExtra("userId") ?: ""
        if (userId.isEmpty()) {
            Toast.makeText(this, "Error: No se recibió el ID de usuario.", Toast.LENGTH_SHORT).show()
            finish() // Salir si no hay userId
            return
        }

        // Cargar los datos del usuario
        loadUserData()

        // Configurar eventos de los botones
        saveButton.setOnClickListener {
            saveUserProfile()
        }

        backButton.setOnClickListener {
            finish() // Regresar a la pantalla anterior
        }
    }

    private fun loadUserData() {
        db.collection("usuarios").document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    nombreField.setText(document.getString("nombre"))
                    cedulaField.setText(document.getString("cedula"))
                    emailField.setText(document.getString("email"))
                    telefonoField.setText(document.getString("telefono"))
                    areaDeTrabajoField.setText(document.getString("areaDeTrabajo"))

                    // Obtener 'dinero' como String y asignarlo al campo
                    val dineroString = document.getString("dinero") ?: "0.0"
                    dineroField.setText(dineroString)
                } else {
                    Toast.makeText(this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al cargar los datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun saveUserProfile() {
        // Validar datos
        if (nombreField.text.isBlank() || emailField.text.isBlank()) {
            Toast.makeText(this, "Por favor, complete todos los campos obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        // Convertir dinero a Double si es posible
        val dineroValue = dineroField.text.toString().toIntOrNull()
        if (dineroValue == null) {
            Toast.makeText(this, "Ingrese un valor numérico válido para dinero", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear un mapa con los datos actualizados
        val updatedData = mapOf(
            "nombre" to nombreField.text.toString(),
            "cedula" to cedulaField.text.toString(),
            "email" to emailField.text.toString(),
            "telefono" to telefonoField.text.toString(),
            "areaDeTrabajo" to areaDeTrabajoField.text.toString(),
            "dinero" to dineroValue.toString() // Convertir a String para mantener la consistencia en Firestore
        )

        // Actualizar el documento en Firestore usando el userId
        db.collection("usuarios").document(userId)
            .update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(this, "Perfil actualizado correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java)) // Redirige a la actividad principal
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error al actualizar el perfil: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
