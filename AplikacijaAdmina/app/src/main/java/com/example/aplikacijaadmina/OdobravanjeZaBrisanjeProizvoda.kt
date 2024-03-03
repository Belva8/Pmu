package com.example.aplikacijaadmina

import android.os.Bundle
import com.google.firebase.database.*
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button


class OdobravanjeZaBrisanjeProizvoda : AppCompatActivity() {

    private lateinit var nazivProizvoda: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.odobravanje_brisanja_narudzbe)

        nazivProizvoda = intent.getStringExtra("naziv") ?: ""

        val Obrisi: Button = findViewById(R.id.Obrisi)
        Obrisi.setOnClickListener {
            obrisiJeloIzFirebase(nazivProizvoda)
        }
    }

    private fun obrisiJeloIzFirebase(nazivJela: String) {
        val database = FirebaseDatabase.getInstance("https://useradminbaserestoran-default-rtdb.europe-west1.firebasedatabase.app/")
        val reference = database.getReference("jelo")

        reference.orderByChild("naziv").equalTo(nazivJela)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (ProizvodSnapshot in snapshot.children) {
                        ProizvodSnapshot.ref.removeValue()
                    }
                    finish()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}