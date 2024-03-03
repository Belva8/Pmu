package com.example.aplikacijaadmina

import android.content.Intent
import android.os.Build.VERSION_CODES.R
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class MainActivity : AppCompatActivity() {
    private lateinit var proizvodAdapter: ProizvodAdapter
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dodajButton: Button = findViewById(R.id.DodajButton)
        dodajButton.setOnClickListener {
            otvoriNovuAktivnost()
        }
        val narudzba: Button = findViewById(R.id.Narudzba)
        narudzba.setOnClickListener {
            val intent = Intent(this, NarudzbaActivity::class.java)
            startActivity(intent)
        }

        val urediProizvod: Button = findViewById(R.id.UrediButton)
        urediProizvod.setOnClickListener {
            val intent = Intent(this, OdabirProizvodaZaUredjivanje::class.java)
            startActivity(intent)
        }

        val izbrisiProizvodButton: Button = findViewById(R.id.IzbrisiButton)
        izbrisiProizvodButton.setOnClickListener {
            otvoriIzbrisiAktivnost()
        }

        // Inicijalizacija RecyclerView-a i Adaptera
        val recyclerView: RecyclerView = findViewById(R.id.rvProizvod)
        proizvodAdapter = ProizvodAdapter(emptyList(), object : ProizvodAdapter.OnItemClickListener {
            override fun onItemClick(Proizvod: Trgovina) {
                val intent = Intent(this@MainActivity, OdobravanjeZaBrisanjeProizvoda::class.java)
                intent.putExtra("naziv", Proizvod.naziv)
                intent.putExtra("cijena", Proizvod.cijena)
                intent.putExtra("imageUrl", Proizvod.imageUrl)
                startActivity(intent)
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = proizvodAdapter // Use proizvodAdapter here instead of ProizvodAdapter

        storageReference = FirebaseStorage.getInstance("gs://useradminbaserestoran.appspot.com").reference.child("slike")

        dohvatiPodatkeZaRecyclerView()
    }

    private fun dohvatiPodatkeZaRecyclerView() {
        val database = FirebaseDatabase.getInstance("https://useradminbaserestoran-default-rtdb.europe-west1.firebasedatabase.app/")
        val reference = database.getReference("Proizvod")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val Proizvodi = mutableListOf<Trgovina>()

                for (ProizvodSnapshot in snapshot.children) {
                    val key = ProizvodSnapshot.key ?: ""
                    val naziv = ProizvodSnapshot.child("naziv").getValue(String::class.java) ?: ""
                    val cijena = ProizvodSnapshot.child("cijena").getValue(String::class.java) ?: ""
                    val imageUrl = ProizvodSnapshot.child("imageUrl").getValue(String::class.java) ?: ""

                    val formattedImageUrl = if (imageUrl.isNotEmpty()) "slike/$imageUrl" else ""

                    val noviProizvod = Trgovina(key, naziv, cijena, formattedImageUrl)
                    Proizvodi.add(noviProizvod)
                }

                proizvodAdapter.ListProizvod = Proizvodi
                proizvodAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Greška prilikom dohvatanja podataka
                Log.e("Firebase", "Greška prilikom dohvatanja podataka: ${error.message}")
            }
        })
    }


    private fun otvoriNovuAktivnost() {
        val intent = Intent(this, DodajProizvod::class.java)
        startActivity(intent)
    }

    private fun otvoriIzbrisiAktivnost() {
        val intent = Intent(this, IzbrisiProizvod::class.java)
        startActivity(intent)
    }
}
