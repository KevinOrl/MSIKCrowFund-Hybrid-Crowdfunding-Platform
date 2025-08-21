package com.example.rs_p2

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.example.rs_p2.R

class ProjectAdapter(
    context: Context,
    private val projectList: List<Map<String, Any>>,
    private val userId: String? // Agregar el idUsuario como parámetro
) : ArrayAdapter<Map<String, Any>>(context, 0, projectList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_project, parent, false)
        }

        val currentProject = projectList[position]

        val projectName = itemView!!.findViewById<TextView>(R.id.project_name)
        projectName.text = currentProject["nombre"]?.toString() ?: "Sin nombre"

        val projectCategory = itemView.findViewById<TextView>(R.id.project_category)
        projectCategory.text = currentProject["categoria"]?.toString() ?: "Sin categoría"

        val detailsButton = itemView.findViewById<Button>(R.id.project_details_button)
        detailsButton.setOnClickListener {
            val creatorId = currentProject["idCreador"]?.toString() ?: run {
                Log.e("ProjectAdapter", "Error: creatorId es nulo o vacío.")
                return@setOnClickListener
            }

            val projectId = currentProject["id"]?.toString() ?: run {
                Log.e("ProjectAdapter", "Error: projectId es nulo o vacío.")
                return@setOnClickListener
            }

            // Crear el Intent para iniciar la actividad de detalles
            val intent = Intent(context, VProject::class.java).apply {
                putExtra("PROJECT_ID", projectId)  // Agregar el ID del proyecto
                putExtra("USER_ID", userId)        // Pasar el ID del usuario
                putExtra("CREATOR_ID", creatorId)
            }

            Log.d("ProjectAdapter", "Iniciando VProject con ID del proyecto: $projectId y usuario: $userId")

            // Iniciar la actividad
            context.startActivity(intent)
        }

        return itemView
    }
}
