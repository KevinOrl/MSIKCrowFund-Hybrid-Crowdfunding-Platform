package com.example.rs_p2

import java.util.Date

data class Project(
    var id: String = "",
    var nombre: String = "",
    var descripcion: String = "",
    var categoria: String = "",
    var objetivo: String = "",
    var montoRecaudado: Int = 0,
    var fechaLimite: Date = Date()
)