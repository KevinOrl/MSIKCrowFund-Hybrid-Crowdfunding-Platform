package com.example.rs_p2

data class Donation(
    val nombreDonador: String = "",
    val correoDonador: String = "",
    val telefonoDonador: String = "",
    val idProyecto: String = "",
    var montoDonado: String = "0.0"
)
