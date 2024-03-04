package com.example.user

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    private lateinit var rvRacuna: RecyclerView
    private lateinit var bankaAdapter: BankaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvRacuna = findViewById(R.id.rvRacuna)

        val layoutManager = LinearLayoutManager(this)
        rvRacuna.layoutManager = layoutManager

        // Initialize RecyclerView and Adapter
        bankaAdapter = BankaAdapter(emptyList()) // Initial empty list, will be populated later
        rvRacuna.adapter = bankaAdapter

        // Retrieve data from Firebase Realtime Database
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

                // Update the Adapter with new data
                bankaAdapter.racunItemList = racuni
                bankaAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Error while fetching data
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }
        })
    }

}
