package com.example.rs_p2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProjectsAdapter(private val projectList: List<AProject>) :
    RecyclerView.Adapter<ProjectsAdapter.ProjectViewHolder>() {

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombreText: TextView = itemView.findViewById(R.id.nombreProyecto)
        val categoriaText: TextView = itemView.findViewById(R.id.categoriaProyecto)
        val descripcionText: TextView = itemView.findViewById(R.id.descripcionProyecto)
        val montoRecaudadoText: TextView = itemView.findViewById(R.id.montoRecaudado)
        val objetivoText: TextView = itemView.findViewById(R.id.objetivo)
        val fechaLimiteText: TextView = itemView.findViewById(R.id.fechaLimite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.project_item, parent, false)
        return ProjectViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        val project = projectList[position]
        holder.nombreText.text = "Nombre del proyecto: ${project.nombre}"
        holder.categoriaText.text = "Categoria del proyecto: ${project.categoria}"
        holder.descripcionText.text = "Descripcion del proyecto: ${project.descripcion}"
        holder.montoRecaudadoText.text = "Monto Recaudado: ${project.montoRecaudado.toString()}"
        holder.objetivoText.text = "Objetivo de recaudacion: ${project.objetivo}"
        holder.fechaLimiteText.text = "Fecha Limite: ${project.fechaLimite}"
    }

    override fun getItemCount() = projectList.size
}
