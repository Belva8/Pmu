package com.example.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {
    private lateinit var RacunAdapter: RacunAdapter
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dodajButton: Button = findViewById(R.id.DodajButton)
        dodajButton.setOnClickListener {
            otvoriNovuAktivnost()
        }

        val izbrisiRacunButton: Button = findViewById(R.id.IzbrisiButton)
        izbrisiRacunButton.setOnClickListener {
            otvoriIzbrisiAktivnost()
        }

        // Inicijalizacija RecyclerView-a i Adaptera
        val recyclerView: RecyclerView = findViewById(R.id.rvRacun)
        RacunAdapter = RacunAdapter(emptyList(), object : RacunAdapter.OnItemClickListener {
            override fun onItemClick(racun: Banka) {
                val intent = Intent(this@MainActivity, OdobravanjeZaBrisanjeRacuna::class.java)
                intent.putExtra("broj", racun.broj)
                intent.putExtra("stanje", racun.stanje)
                intent.putExtra("imageUrl", racun.imageUrl)
                startActivity(intent)
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = racunAdapter
        storageReference = FirebaseStorage.getInstance("https://racun-b1e0f-default-rtdb.europe-west1.firebasedatabase.app/.com").getReference("slike")

        // Dohvat podataka iz Firebase Realtime Database-a
        dohvatiPodatkeZaRecyclerView()
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

                // Ažuriranje Adaptera s novim podacima
                racunAdapter.foodItemList = racuni
                racunAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Greška prilikom dohvatanja podataka
                Log.e("Firebase", "Greška prilikom dohvatanja podataka: ${error.message}")
            }
        })
    }

    private fun otvoriNovuAktivnost() {
        val intent = Intent(this, DodajRacun::class.java)
        startActivity(intent)
    }

    private fun otvoriIzbrisiAktivnost() {
        val intent = Intent(this, IzbrisiRacun::class.java)
        startActivity(intent)
    }
}
