package com.example.rs_p2

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class Profile : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userName = findViewById<TextView>(R.id.userName)
        val userArea = findViewById<TextView>(R.id.userArea)
        val userPhone = findViewById<TextView>(R.id.userPhone)
        val userEmail = findViewById<TextView>(R.id.userEmail)
        val userMoney = findViewById<TextView>(R.id.userMoney)

        val donationsButton = findViewById<Button>(R.id.donationsButton)
        val editButton = findViewById<Button>(R.id.editButton)
        val backButton = findViewById<Button>(R.id.backButton)

        // Obtener el ID de usuario desde el Intent
        val userId = intent.getStringExtra("USER_ID") // Asegúrate de que userId fue pasado desde la actividad anterior

        if (userId != null) {
            db.collection("usuarios").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        userName.text = document.getString("nombre")
                        userArea.text = document.getString("areaDeTrabajo")
                        userPhone.text = document.getString("telefono")
                        userEmail.text = document.getString("email")
                        userMoney.text = document.getString("dinero")
                    } else {
                        Toast.makeText(this, "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error obteniendo datos: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "Error: No se recibió ID de usuario", Toast.LENGTH_SHORT).show()
        }

        donationsButton.setOnClickListener {
            val intent = Intent(this, MyDonations::class.java)
            intent.putExtra("idUsuario", userId)
            startActivity(intent)
        }

        editButton.setOnClickListener {
            val intent = Intent(this, EditProfile::class.java)
            intent.putExtra("userId", userId) // Pasa el Id del usuario al editar
            startActivity(intent)
        }

        backButton.setOnClickListener {
            finish()
        }
    }
}


//Seccion para el caso de prueba

//package com.example.rs_p2
//
//import android.content.Intent
//import android.os.Bundle
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//
//class Profile : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_profile)
//
//        val userName = findViewById<TextView>(R.id.userName)
//        val userArea = findViewById<TextView>(R.id.userArea)
//        val userPhone = findViewById<TextView>(R.id.userPhone)
//        val userEmail = findViewById<TextView>(R.id.userEmail)
//        val userMoney = findViewById<TextView>(R.id.userMoney)
//
//        val donationsButton = findViewById<Button>(R.id.donationsButton)
//        val editButton = findViewById<Button>(R.id.editButton)
//        val backButton = findViewById<Button>(R.id.backButton)
//
//        // Simular datos del usuario para pruebas
//        userName.text = "Sebastian Rodriguez"
//        userArea.text = "Estudiante"
//        userPhone.text = "12121212"
//        userEmail.text = "seb_r@estudiantec.cr"
//        userMoney.text = "$10000000"
//
//        donationsButton.setOnClickListener {
//            val ventDonaciones = Intent(this, MyDonations::class.java).putExtra("idUsuario", "9JQDQfgXPejqs2XriEKx")
//            startActivity(ventDonaciones)
//        }
//
//        editButton.setOnClickListener {
//            val intent = Intent(this, EditProfile::class.java)
//            intent.putExtra("userId", "uuQjtlK7bHzn82lOsEmy") // ID de usuario simulado
//            startActivity(intent)
//        }
//
//        backButton.setOnClickListener {
//            finish()
//        }
//    }
//}
//
