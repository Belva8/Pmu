package com.example.aplikacijaadmina

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import android.content.Intent
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.*


class NarudzbaActivity : AppCompatActivity(), NarudzbaAdapter.OnItemClickListener {

    private lateinit var narudzbaAdapter: NarudzbaAdapter
    private lateinit var zahtjeviReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dodavanje_narudzbe)

        val recyclerView: RecyclerView = findViewById(R.id.RVPopisProizvoda)
        val narudzbaList = mutableListOf<Zahtjev>()
        narudzbaAdapter = NarudzbaAdapter(this) // Pass 'this' as the listener

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = narudzbaAdapter

        zahtjeviReference = FirebaseDatabase.getInstance("https://useradminbaserestoran-default-rtdb.europe-west1.firebasedatabase.app/").getReference("zahtjevi")
        // Fetch data from Firebase
        fetchNarudzbaData()
    }

    override fun onItemClick(zahtjev: Zahtjev) {
        val intent = Intent(this, OdobravanjeNarudzbe::class.java)
        intent.putExtra("korisnikId", zahtjev.korisnikId)
        intent.putExtra("ime", zahtjev.ime)
        intent.putExtra("email", zahtjev.email)
        intent.putExtra("nazivProizvoda", zahtjev.nazivProizvoda)
        intent.putExtra("cijenaProizvoda", zahtjev.cijenaProizvoda)
        startActivity(intent)
    }

    private fun fetchNarudzbaData() {
        zahtjeviReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val narudzbaItems = mutableListOf<Zahtjev>()

                for (zahtjevSnapshot in snapshot.children) {
                    val korisnikId = zahtjevSnapshot.child("korisnikId").getValue(String::class.java) ?: ""
                    val ime = zahtjevSnapshot.child("ime").getValue(String::class.java) ?: ""
                    val email = zahtjevSnapshot.child("email").getValue(String::class.java) ?: ""
                    val nazivProizvoda = zahtjevSnapshot.child("nazivProizvoda").getValue(String::class.java) ?: ""
                    val cijenaProizvoda = zahtjevSnapshot.child("cijenaProizvoda").getValue(String::class.java) ?: ""
                    val odobren = zahtjevSnapshot.child("odobren").getValue(Int::class.java) ?: 0

                    if (odobren != 1) {
                        val zahtjev = Zahtjev(korisnikId, ime, email, nazivProizvoda, cijenaProizvoda, odobren)
                        narudzbaItems.add(zahtjev)
                    }
                }

                narudzbaAdapter.narudzbaList = narudzbaItems
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }
        })
    }
}