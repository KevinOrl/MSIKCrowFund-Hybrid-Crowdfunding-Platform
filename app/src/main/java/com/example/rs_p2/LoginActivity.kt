package com.example.rs_p2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailInput = findViewById<EditText>(R.id.email_input)
        val passwordInput = findViewById<EditText>(R.id.password_input)
        val createAccountBtn = findViewById<Button>(R.id.create_account_btn)
        val LoginInBtn = findViewById<Button>(R.id.login_btn)
        val forgotPassword = findViewById<TextView>(R.id.forgot_password)

        LoginInBtn.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                db.collection("usuarios")
                    .whereEqualTo("email", email)
                    .get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val userDoc = documents.documents[0]
                            val userData = userDoc.data
                            val isActive = userData?.get("activo") as? Boolean ?: false

                            if (isActive) {
                                auth.signInWithEmailAndPassword(email, password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            val isAdmin = userData?.get("admin") as? Boolean ?: false
                                            if (isAdmin) {
                                                startActivity(Intent(this, AdminActivity::class.java))
                                            } else {
                                                // Obtener el idUsuario del documento
                                                val userId = userDoc.id
                                                // Pasar el idUsuario a LProject
                                                val intent = Intent(this, LProject::class.java)
                                                intent.putExtra("USER_ID", userId)
                                                startActivity(intent)
                                            }
                                        } else {
                                            Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(this, "Usuario inactivo", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Por favor ingrese todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        createAccountBtn.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }

        forgotPassword.setOnClickListener {
            // Implementar lógica de recuperación de contraseña
            Toast.makeText(this, "Funcionalidad de recuperación no implementada", Toast.LENGTH_SHORT).show()
        }
    }
}