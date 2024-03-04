package com.example.admin

data class zahtjev(
    val userId: String,
    val ime: String,
    val e-mail: String,
    val BrojRacuna: String,
    val StanjeRacuna: String,
    val Odobreno: Boolean = false
)