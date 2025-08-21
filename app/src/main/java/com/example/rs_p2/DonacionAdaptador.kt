package com.example.rs_p2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DonacionAdaptador(private val donaciones: List<Donacion>) :
    RecyclerView.Adapter<DonacionAdaptador.DonacionViewHolder>() {

    class DonacionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNombreProyecto: TextView = view.findViewById(R.id.tvNombreProyecto)
        val tvMonto: TextView = view.findViewById(R.id.tvMonto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonacionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.donacion, parent, false)
        return DonacionViewHolder(view)
    }

    override fun onBindViewHolder(holder: DonacionViewHolder, position: Int) {
        val donacion = donaciones[position]
        holder.tvNombreProyecto.text = donacion.nombreProyecto
        holder.tvMonto.text = donacion.monto
    }

    override fun getItemCount() = donaciones.size
}
