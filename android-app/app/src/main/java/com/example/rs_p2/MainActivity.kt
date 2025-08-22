package com.example.rs_p2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    // Instancia de Firebase Firestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGoToLogin = findViewById<Button>(R.id.btnGoToLogin)
        // Configura el click listener
        btnGoToLogin.setOnClickListener {
            // Crea un Intent para iniciar SignupActivity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val btnGoToSignup = findViewById<Button>(R.id.btnGoToSignup)
        // Configura el click listener
        btnGoToSignup.setOnClickListener {
            // Crea un Intent para iniciar SignupActivity
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}
