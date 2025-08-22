package com.example.rs_p2

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class LProject : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var projectListView: ListView
    private lateinit var searchInput: EditText
    private lateinit var selectedDateText: TextView
    private var startDate: String? = null
    private val proyectos = mutableListOf<Map<String, Any>>()
    private var userId: String? = null // Variable para almacenar el ID del usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lista_project)

        db = FirebaseFirestore.getInstance()
        userId = intent.getStringExtra("USER_ID") // Obtener el ID del usuario desde el Intent

        projectListView = findViewById(R.id.project_list)
        searchInput = findViewById(R.id.search_input)
        selectedDateText = findViewById(R.id.selected_date_text)

        val searchButton: Button = findViewById(R.id.search_button)
        val datePickerButton: Button = findViewById(R.id.date_picker_button)
        val createProjectButton : Button = findViewById(R.id.create_project_button)
        val profileButton: ImageButton = findViewById(R.id.perfil_button)

        // Buscar proyectos por nombre o categoría
        searchButton.setOnClickListener {
            val searchQuery = searchInput.text.toString()
            searchProjects(searchQuery)
        }

        // Abrir DatePicker para seleccionar la fecha
        datePickerButton.setOnClickListener {
            openDatePicker()
        }

        //Función para ir a crear un proyecto nuevo
        createProjectButton.setOnClickListener{
            val ventNewProject = Intent(this, NewProject::class.java)
            startActivity(ventNewProject)
        }

        //Función para ir a perfil de un usuario
        profileButton.setOnClickListener{
            val ventProfile = Intent(this, Profile::class.java).apply{
                putExtra("USER_ID", userId)
            }
            startActivity(ventProfile)
        }
    }

    // Función para obtener proyectos por nombre o categoría
    private fun searchProjects(searchQuery: String) {
        val proyCollection = db.collection("proyectos")
        proyCollection.whereGreaterThanOrEqualTo("nombre", searchQuery)
            .get()
            .addOnSuccessListener { documents ->
                proyectos.clear()
                for (document in documents) {
                    val projectData = document.data.toMutableMap() // Convierte los datos a un Map mutable
                    projectData["id"] = document.id  // Agrega el ID del documento a los datos
                    proyectos.add(projectData)
                }
                updateProjectList()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error obteniendo datos: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    // Función para abrir el DatePicker y seleccionar la fecha límite
    private fun openDatePicker() {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                startDate = dateFormat.format(selectedDate.time)
                selectedDateText.text = "Fecha seleccionada: $startDate"
                searchProjectsByDate(startDate!!)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    // Función para obtener proyectos por fecha límite
    private fun searchProjectsByDate(date: String) {
        val proyCollection = db.collection("proyectos")
        proyCollection.whereLessThanOrEqualTo("fechaLimite", date)
            .get()
            .addOnSuccessListener { documents ->
                proyectos.clear()
                for (document in documents) {
                    val projectData = document.data.toMutableMap()  // Convierte los datos a un Map mutable
                    projectData["id"] = document.id  // Agrega el ID del documento a los datos
                    proyectos.add(projectData)
                }
                updateProjectList()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error obteniendo datos: $exception", Toast.LENGTH_SHORT).show()
            }
    }

    // Función para actualizar la lista de proyectos
    private fun updateProjectList() {
        val adapter = ProjectAdapter(this, proyectos, userId) // Usa el adaptador personalizado
        projectListView.adapter = adapter
    }
}