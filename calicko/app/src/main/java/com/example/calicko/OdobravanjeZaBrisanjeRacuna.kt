package com.example.admin
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class OdobravanjeZaBrisanjeRacuna : AppCompatActivity() {

    private lateinit var brojRacuna: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_odobravanje_za_brisanje_racuna)

        // Dobivanje podataka iz Intenta
        brojRacuna = intent.getStringExtra("broj") ?: ""

        val btnObrisi: Button = findViewById(R.id.btnObrisi)
        btnObrisi.setOnClickListener {
            obrisiRacunIzFirebase(brojRacuna)
        }
    }

    private fun obrisiRacunIzFirebase(brojRacuna: String) {
        val database = FirebaseDatabase.getInstance("https://racun-b1e0f-default-rtdb.europe-west1.firebasedatabase.app/")
        val reference = database.getReference("racun")

        // Pronalaženje ključa racuna koje želimo obrisati prema nazivu
        reference.orderByChild("broj").equalTo(brojRacuna)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (racunSnapshot in snapshot.children) {
                        // Brisanje pronađenog racuna iz Firebase-a
                        racunSnapshot.ref.removeValue()
                    }
                    // Zatvaranje aktivnosti nakon brisanja
                    finish()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Greška prilikom dohvata podataka
                    // Ovdje možete implementirati odgovarajući postupak u slučaju greške
                }
            })
    }
}
