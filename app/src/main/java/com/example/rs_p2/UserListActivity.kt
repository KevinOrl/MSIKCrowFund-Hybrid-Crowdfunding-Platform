package com.example.rs_p2

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class UserListActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private lateinit var userAdapter: UserAdapter
    private val userList = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        userAdapter = UserAdapter(userList, { user -> toggleEstado(user) }, { user, newRole -> updateRole(user, newRole) })
        recyclerView.adapter = userAdapter

        val btnRegresar: Button = findViewById(R.id.btnRegresar)
        btnRegresar.setOnClickListener {
            finish() // Regresa a la pantalla anterior
        }

        fetchUsers()
    }

    private fun fetchUsers() {
        db.collection("usuarios")
            .get()
            .addOnSuccessListener { documents ->
                userList.clear()
                for (document in documents) {
                    val user = document.toObject(User::class.java).apply {
                        id = document.id
                    }
                    userList.add(user)
                }
                userAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                Log.w("UserListActivity", "Error al cargar usuarios", e)
            }
    }

    private fun toggleEstado(user: User) {
        val userRef = db.collection("usuarios").document(user.id)
        userRef.update("activo", !user.activo)
            .addOnSuccessListener {
                Toast.makeText(this, "Estado actualizado", Toast.LENGTH_SHORT).show()
                fetchUsers() // Refrescar la lista
            }
            .addOnFailureListener { e ->
                Log.w("UserListActivity", "Error al actualizar estado", e)
            }
    }

    private fun updateRole(user: User, newRole: String) {
        val userRef = db.collection("usuarios").document(user.id)
        userRef.update("rol", newRole)
            .addOnSuccessListener {

                fetchUsers()
            }
            .addOnFailureListener { e ->
                Log.w("UserListActivity", "Error al actualizar rol", e)
            }
    }
}