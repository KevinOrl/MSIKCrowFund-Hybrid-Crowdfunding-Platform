package com.example.rs_p2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.app.DatePickerDialog
import android.widget.Toast
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat

class EditProject : AppCompatActivity() {
    // Instancia de Firebase Firestore
    private val db = FirebaseFirestore.getInstance()

    //variables para almacenar id de creador y montoRecaudado
    private lateinit var idCreador : String
    private lateinit var recaudadoString : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_project)

        //Recibe el parametro que contiene el id del proyecto a editar
        val idProyecto = intent.getStringExtra("PROJECT_ID").toString()

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
        val textFieldMonto = findViewById<EditText>(R.id.monto)
        val textFieldFecha = findViewById<EditText>(R.id.fecha)

        //Validar que los campos antes de obtener los datos estén llenos
        fun validarCampos(vararg campos: EditText): Boolean {
            for (campo in campos) {
                if (campo.text.toString().trim().isEmpty()) {
                    return false
                }
            }
            return true
        }

        //Obtener y rellenar los datos del proyecto
        db.collection("proyectos").document(idProyecto).get()
            .addOnSuccessListener { document ->
                if(document != null){
                    textFieldNombre.setText(document.getString("nombre"))
                    textFieldDesc.setText(document.getString("descripcion"))
                    textFieldCate.setText(document.getString("categoria"))
                    textFieldMonto.setText(document.getDouble("objetivo")?.toString())
                    textFieldFecha.setText(document.getString("fechaLimite"))

                    idCreador = document.getString("idCreador").toString()
                    recaudadoString = document.getDouble("montoRecaudado").toString()
                }
            }
            .addOnFailureListener{ e ->
                Toast.makeText(this, "Error al obtener los datos: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        textFieldFecha.setOnClickListener{
            showDatePickerDialog()
        }

        //Botón de editar el proyecto y actualizarlo en la db
        val btnEditar = findViewById<Button>(R.id.btnEditar)
        btnEditar.setOnClickListener{
            if(validarCampos(textFieldNombre, textFieldDesc, textFieldCate, textFieldMonto, textFieldFecha)){
                // Leer los datos de los EditText por si se actualizaron
                val nombreProyecto = textFieldNombre.text.toString()
                val descripcion = textFieldDesc.text.toString()
                val categoria = textFieldCate.text.toString()
                val objetivoFinanciacion = textFieldMonto.text.toString()
                val fechaLimite = textFieldFecha.text.toString()

                if(!validarFecha(fechaLimite)){
                    Toast.makeText(this, "La fecha límite debe ser mayor a la actual", Toast.LENGTH_SHORT).show()
                }
                else{
                    val monto = objetivoFinanciacion.toFloat()
                    val recaudado = recaudadoString.toFloat()

                    val proyecto = hashMapOf(
                        "nombre" to nombreProyecto,
                        "descripcion" to descripcion,
                        "categoria" to categoria,
                        "objetivo" to monto,
                        "idCreador" to idCreador,
                        "montoRecaudado" to recaudado,
                        "fechaLimite" to fechaLimite

                    )

                    db.collection("proyectos").document(idProyecto).set(proyecto)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Proyecto actualizado exitosamente", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                        .addOnFailureListener{ e ->
                            Toast.makeText(this, "Error al actualizar el proyecto: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            else{
                Toast.makeText(this, "No deben haber campos vacíos para realizar esta acción", Toast.LENGTH_SHORT).show()
            }

        }

        //Se devuelve a la ventana anterior
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener{
            finish()
        }
    }
}