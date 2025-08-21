package com.example.rs_p2

// DonationAdapter.kt
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DonationAdapter(private val donationList: List<Donation>, private val proyectos: Map<String, String>) :
    RecyclerView.Adapter<DonationAdapter.DonationViewHolder>() {

    class DonationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreDonador: TextView = view.findViewById(R.id.nombreDonador)
        val correoDonador: TextView = view.findViewById(R.id.correoDonador)
        val telefonoDonador: TextView = view.findViewById(R.id.telefonoDonador)
        val nombreProyecto: TextView = view.findViewById(R.id.nombreProyecto)
        val montoDonado: TextView = view.findViewById(R.id.montoDonado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.donation_item, parent, false)
        return DonationViewHolder(view)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val donation = donationList[position]
        holder.nombreDonador.text = "Nombre del donador: ${donation.nombreDonador}"
        holder.correoDonador.text = "Correo del donador: ${donation.correoDonador}"
        holder.telefonoDonador.text = "Teléfono del donador: ${donation.telefonoDonador}"
        holder.nombreProyecto.text = "Nombre del proyecto: ${donation.idProyecto}"
        holder.montoDonado.text = "Monto donado: ${donation.montoDonado}"
    }

    override fun getItemCount() = donationList.size
}
