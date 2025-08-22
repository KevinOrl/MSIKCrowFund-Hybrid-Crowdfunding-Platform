package com.example.rs_p2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

class NewProject : AppCompatActivity() {
    // Instancia de Firebase Firestore
    private val db = FirebaseFirestore.getInstance()
    private lateinit var emailUser: String
    private lateinit var idCurrentUser: String
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_project)

        //obtener email del usuario en linea actual para luego buscar el id
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // Usuario está autenticado, obtenemos su email
            emailUser = currentUser.email.toString()
        }
        else{
            Toast.makeText(this, "no hay usuario", Toast.LENGTH_SHORT).show()
        }

        //obtener el id del usuario en linea con el correo
        db.collection("usuarios").whereEqualTo("email", emailUser).get()
            .addOnSuccessListener { documents ->
                for (document in documents){
                    idCurrentUser = document.id
                }
            }
            .addOnFailureListener{ e ->
                Toast.makeText(this, "Error al obtener los datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }

        //Función para mostrar calendario
        fun showDatePickerDialog() {
            val calendario = Calendar.getInstance()
            val anno = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)

            val selectCalen = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->
                val selectedDate = "$year-${monthOfYear + 1}-$dayOfMonth"
                val selectFecha = findViewById<EditText>(R.id.fecha)
                selectFecha.setText(selectedDate)
            }, anno, mes, dia)

            selectCalen.show()
        }

        //muestra calendario para introducir la fecha
        val selectFecha = findViewById<EditText>(R.id.fecha)
        selectFecha.setOnClickListener {
            showDatePickerDialog()
        }

        //Se devuelve a la ventana anterior
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener{
            finish()
        }

        //Función para vaciar los campos despues delinsert en la base
        fun limpiarCampos(vararg campos: EditText) {
            for (campo in campos) {
                campo.text.clear()
            }
        }

        //Válida que la fecha sea corecta, o sea, mayor a la actual
        fun validarFecha(fecha: String): Boolean{
            val formato = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val fechaValidar = formato.parse(fecha)
            val fechaActual = Calendar.getInstance().time

            return fechaValidar.after(fechaActual)
        }

        // Referencias a los EditText
        val textFieldNombre = findViewById<EditText>(R.id.nombre)
        val textFieldDesc = findViewById<EditText>(R.id.descripcion)
        val textFieldCate = findViewById<EditText>(R.id.categoria)
        val textField4Monto = findViewById<EditText>(R.id.monto)
        val textFieldFecha = findViewById<EditText>(R.id.fecha)

        // Botón de crear proyecto y guardarlo en la base
        val btnCrear = findViewById<Button>(R.id.btnCrear)
        btnCrear.setOnClickListener {

            // Leer los datos de los EditText
            val nombreProyecto = textFieldNombre.text.toString()
            val descripcion = textFieldDesc.text.toString()
            val categoria = textFieldCate.text.toString()
            val objetivoFinanciacion = textField4Monto.text.toString()
            val fechaLimite = textFieldFecha.text.toString()

            if(nombreProyecto.isEmpty() || descripcion.isEmpty() || categoria.isEmpty() || objetivoFinanciacion.isEmpty() || fechaLimite.isEmpty()){
                Toast.makeText(this, "Para crear un proyecto nuevo todos los campos deben estár llenos", Toast.LENGTH_SHORT).show()
            }
            else {
                if(!validarFecha(fechaLimite)){
                    Toast.makeText(this, "La fecha límite debe ser mayor a la fecha actual", Toast.LENGTH_SHORT).show()
                }
                else {
                    val monto = objetivoFinanciacion.toFloat()

                    // Crear un mapa con los datos
                    val proyecto = hashMapOf(
                        "nombre" to nombreProyecto,
                        "descripcion" to descripcion,
                        "categoria" to categoria,
                        "objetivo" to monto,
                        "idCreador" to idCurrentUser,
                        "montoRecaudado" to 0,
                        "fechaLimite" to fechaLimite
                    )

                    // Enviar los datos a Firestore
                    db.collection("proyectos")
                        .add(proyecto)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Proyecto guardado exitosamente",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            limpiarCampos(
                                textFieldNombre,
                                textFieldDesc,
                                textFieldCate,
                                textField4Monto,
                                textFieldFecha
                            )
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Error al guardar el proyecto: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
        }
    }
}