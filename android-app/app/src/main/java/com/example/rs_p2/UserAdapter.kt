package com.example.rs_p2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(
    private val users: List<User>,
    private val onToggleClick: (User) -> Unit,
    private val onRoleChange: (User, String) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.nombreTextView)
        val cedulaTextView: TextView = view.findViewById(R.id.cedulaTextView)
        val rolTextView: TextView = view.findViewById(R.id.rolTextView)
        val estadoTextView: TextView = view.findViewById(R.id.estadoTextView)
        val toggleButton: Button = view.findViewById(R.id.toggleButton)
        val rolSpinner: Spinner = view.findViewById(R.id.rolSpinner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]
        holder.nombreTextView.text = "Nombre de usuario: ${user.nombre}"
        holder.cedulaTextView.text = "Cedula: ${user.cedula}"
        holder.rolTextView.text = "Rol: ${user.rol}"
        holder.estadoTextView.text = if (user.activo) "Activo" else "Desactivo"

        holder.toggleButton.setOnClickListener {
            onToggleClick(user)
        }

        holder.rolSpinner.setSelection(getRoleIndex(user.rol))
        holder.rolSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedRole = parent?.getItemAtPosition(position).toString()
                if (selectedRole != user.rol) { // Solo actualizar si el rol ha cambiado
                    onRoleChange(user, selectedRole)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

    }

    private fun getRoleIndex(role: String): Int {
        val roles = arrayOf("Encargado", "Supervisor", "Administrador", "Analista")
        return roles.indexOf(role).takeIf { it >= 0 } ?: 0
    }

    override fun getItemCount(): Int = users.size
}
