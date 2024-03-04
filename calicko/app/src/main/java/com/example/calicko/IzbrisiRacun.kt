package com.example.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class IzbrisiRacun : AppCompatActivity(), RacunAdapter.OnItemClickListener {

    private lateinit var RacunAdapter: RacunAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_izbrisi_racun)

        val recyclerView: RecyclerView = findViewById(R.id.rvIzbrisiRacun)

        // Inicijalizacija adaptera s implementacijom OnItemClickListener
        racunAdapter = RacunAdapterAdapter(emptyList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = racunAdapter

        // Dohvat podataka iz Firebase Realtime Database-a
        dohvatiPodatkeZaRecyclerView()
    }

    override fun onItemClick(racun: Banka) {
        // Otvorite aktivnost za odobravanje brisanja, proslijedite podatke
        val intent = Intent(this, OdobravanjeZaBrisanjeRacuna::class.java)
        intent.putExtra("broj", racun.broj)
        intent.putExtra("stanje", racun.stanje)
        intent.putExtra("imageUrl", racun.imageUrl)
        startActivity(intent)
    }

    private fun dohvatiPodatkeZaRecyclerView() {
        val database = FirebaseDatabase.getInstance("https://racun-b1e0f-default-rtdb.europe-west1.firebasedatabase.app/")
        val reference = database.getReference("racun")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val racuna = mutableListOf<Banka>()

                for (racunSnapshot in snapshot.children) {
                    val broj = racunSnapshot.child("broj").getValue(String::class.java) ?: ""
                    val stanje = racunSnapshot.child("stanje").getValue(String::class.java) ?: ""
                    val imageUrl = racunSnapshot.child("imageUrl").getValue(String::class.java) ?: ""

                    val noviRacun = Banka(broj, stanje, imageUrl)
                    racuni.add(noviRacun)
                }

                racunAdapter.racunItemList = racuni
                racunAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Greška prilikom dohvatanja podataka
                Log.e("Firebase", "Greška prilikom dohvatanja podataka: ${error.message}")
            }
        })
    }
}
