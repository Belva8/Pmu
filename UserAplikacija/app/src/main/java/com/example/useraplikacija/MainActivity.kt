package com.example.useraplikacija

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {
    private lateinit var rvProizvoda: RecyclerView
    private lateinit var trgovinaAdapter: TrgovinaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvProizvoda = findViewById(R.id.rvProizvoda)

        val layoutManager = LinearLayoutManager(this)
        rvProizvoda.layoutManager = layoutManager

        // Initialize RecyclerView and Adapter
        trgovinaAdapter = TrgovinaAdapter(emptyList()) // Initial empty list, will be populated later
        rvProizvoda .adapter = trgovinaAdapter

        // Retrieve data from Firebase Realtime Database
        dohvatiPodatkeZaRecyclerView()
    }

    private fun dohvatiPodatkeZaRecyclerView() {
        val database = FirebaseDatabase.getInstance("https://useradminbaserestoran-default-rtdb.europe-west1.firebasedatabase.app/")
        val reference = database.getReference("proizvod")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val proizvod = mutableListOf<Trgovina>()

                for (proizvodSnapshot in snapshot.children) {
                    val naziv = proizvodSnapshot.child("naziv").getValue(String::class.java) ?: ""
                    val cijena = proizvodSnapshot.child("cijena").getValue(String::class.java) ?: ""
                    val imageUrl = proizvodSnapshot.child("imageUrl").getValue(String::class.java) ?: ""

                    val noviProizvod = Trgovina(naziv, cijena, imageUrl)
                    proizvod.add(noviProizvod)
                }

                // Update the Adapter with new data
                trgovinaAdapter.ProizvodItemList = proizvod
                trgovinaAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Error while fetching data
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }
        })
    }

}