package com.example.aplikacijaadmina

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class IzbrisiProizvod : AppCompatActivity(), ProizvodAdapter.OnItemClickListener {

    private lateinit var ProizvodAdapter: ProizvodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.brisanje_proizvoda)

        val recyclerView: RecyclerView = findViewById(R.id.RVBrisanjeProizvoda)

        // Inicijalizacija adaptera s implementacijom OnItemClickListener
        ProizvodAdapter = ProizvodAdapter(emptyList(), this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ProizvodAdapter

        // Dohvat podataka iz Firebase Realtime Database-a
        dohvatiPodatkeZaRecyclerView()
    }

    override fun onItemClick(jelo: Restoran) {
        // Otvorite aktivnost za odobravanje brisanja, proslijedite podatke
        val intent = Intent(this, OdobravanjeZaBrisanjeJela::class.java)
        intent.putExtra("key", jelo.key) // Dodan ključ jela
        intent.putExtra("naziv", jelo.naziv)
        intent.putExtra("cijena", jelo.cijena)
        intent.putExtra("imageUrl", jelo.imageUrl)
        startActivity(intent)
    }

    private fun dohvatiPodatkeZaRecyclerView() {
        val database = FirebaseDatabase.getInstance("https://useradminbaserestoran-default-rtdb.europe-west1.firebasedatabase.app/")
        val reference = database.getReference("jelo")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jela = mutableListOf<Restoran>()

                for (jeloSnapshot in snapshot.children) {
                    val key = jeloSnapshot.key ?: ""
                    val naziv = jeloSnapshot.child("naziv").getValue(String::class.java) ?: ""
                    val cijena = jeloSnapshot.child("cijena").getValue(String::class.java) ?: ""
                    val imageUrl = jeloSnapshot.child("imageUrl").getValue(String::class.java) ?: ""

                    val novoJelo = Restoran(key, naziv, cijena, imageUrl)
                    jela.add(novoJelo)
                }

                jeloAdapter.foodItemList = jela
                jeloAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Greška prilikom dohvatanja podataka
                Log.e("Firebase", "Greška prilikom dohvatanja podataka: ${error.message}")
            }
        })
    }
}