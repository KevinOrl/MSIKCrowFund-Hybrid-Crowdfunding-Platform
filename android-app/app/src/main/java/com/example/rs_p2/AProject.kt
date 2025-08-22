package com.example.rs_p2

data class AProject(
    val id: String = "",
    val nombre: String = "",
    val categoria: String = "",
    val descripcion: String = "",
    val montoRecaudado: Double = 0.0,
    var objetivo: String = "0.0",
    val fechaLimite: String = ""
)
