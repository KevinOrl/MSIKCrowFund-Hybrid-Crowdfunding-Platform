package com.example.rs_p2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Inicializar Firebase Auth y Firestore
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val btnRegistrarse: Button = findViewById(R.id.btnRegistrarse)
        val inputEmail: EditText = findViewById(R.id.inputEmail)
        val inputNombre: EditText = findViewById(R.id.inputNombre)
        val inputContrasenna: EditText = findViewById(R.id.inputContrasenna)
        val inputCedula: EditText = findViewById(R.id.inputCedula)
        val inputTelefono: EditText = findViewById(R.id.inputTelefono)
        val inputAreaDeTrabajo: EditText = findViewById(R.id.inputAreaDeTrabajo)
        val inputDinero: EditText = findViewById(R.id.inputDinero)

        btnRegistrarse.setOnClickListener {
            val email = inputEmail.text.toString()
            val contrasenna = inputContrasenna.text.toString()

            if (!validateEmail(email)) {
                Toast.makeText(this, "Los únicos dominios permitidos son @estudiantec y @itcr", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, contrasenna)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = hashMapOf(
                            "nombre" to inputNombre.text.toString(),
                            "email" to email,
                            "contrasenna" to contrasenna,
                            "cedula" to inputCedula.text.toString(),
                            "telefono" to inputTelefono.text.toString(),
                            "areaDeTrabajo" to inputAreaDeTrabajo.text.toString(),
                            "dinero" to inputDinero.text.toString(),
                            "activo" to true,
                            "admin" to false
                        )

                        db.collection("usuarios").add(user)
                            .addOnSuccessListener {
                                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                                finish() // Termina la actividad y regresa
                            }
                            .addOnFailureListener { e ->
                                Log.e("SignupActivity", "Error al registrar usuario", e)
                                Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        Log.e("SignupActivity", "Error al crear usuario", task.exception)
                        Toast.makeText(this, "Error al crear usuario", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        val btnIniciarSesion = findViewById<Button>(R.id.btnIniciarSesion)
        // Configura el click listener
        btnIniciarSesion.setOnClickListener {
            // Crea un Intent para iniciar SignupActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val btnRegresar = findViewById<Button>(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            // Vuelve a la actividad de inicio de sesión
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Finaliza la actividad actual
        }
    }


    private fun validateEmail(email: String): Boolean {
        val validDomains = listOf("@estudiantec.cr", "@itcr.cr")
        return validDomains.any { email.endsWith(it) }
    }
}