package com.example.rs_p2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)

        // Configura las acciones para los botones
        val btnMonitoreoProyectos = findViewById<Button>(R.id.btnMonitoreoProyectos)
        val btnMonitoreoDonaciones = findViewById<Button>(R.id.btnMonitoreoDonaciones)
        val btnGestionUsuarios = findViewById<Button>(R.id.btnGestionUsuarios)
        val btnEstadisticasSistema = findViewById<Button>(R.id.btnEstadisticasSistema)
        val btnRegresar = findViewById<Button>(R.id.btnRegresar)

        btnMonitoreoProyectos.setOnClickListener {
            // Navega a la actividad de Monitoreo de Proyectos
            startActivity(Intent(this, ProjectListActivity::class.java))
        }

        btnMonitoreoDonaciones.setOnClickListener {
            // Navega a la actividad de Monitoreo de Donaciones
            startActivity(Intent(this, AdminDonationsActivity::class.java))
        }

        btnGestionUsuarios.setOnClickListener {
            // Navega a la actividad de Gestión de Usuarios
            startActivity(Intent(this, UserListActivity::class.java))
        }

        btnEstadisticasSistema.setOnClickListener {
            // Navega a la actividad de Estadísticas del Sistema
            startActivity(Intent(this, EstadisticasActivity::class.java))
        }

        btnRegresar.setOnClickListener {
            // Vuelve a la actividad de inicio de sesión
            startActivity(Intent(this, LoginActivity::class.java))
            finish() // Finaliza la actividad actual
        }
    }
}
