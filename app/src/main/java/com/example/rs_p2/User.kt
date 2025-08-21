package com.example.rs_p2

data class User(
    var id: String = "",
    val nombre: String = "",
    val correo: String = "",
    val cedula: String = "",
    val rol: String = "",
    val activo: Boolean = true
)