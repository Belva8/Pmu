package com.example.aplikacijaadmina

data class Zahtjev(
    val korisnikId: String,
    val ime: String,
    val email: String,
    val nazivProizvoda: String,
    val cijenaProizvoda: String,
    val odobren: Int
)