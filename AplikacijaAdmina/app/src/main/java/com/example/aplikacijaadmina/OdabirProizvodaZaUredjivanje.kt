package com.example.aplikacijaadmina


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference




class OdabirProizvodaZaUredjivanje  : AppCompatActivity() {
    private lateinit var ProizvodAdapter: ProizvodAdapter
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.uredivanje_narudzbe)

        // RecyclerView setup
        val recyclerView: RecyclerView = findViewById(R.id.RVPopisProizvodaUredjivanje)
        recyclerView.layoutManager = LinearLayoutManager(this)
        ProizvodAdapter = ProizvodAdapter(emptyList(), object : ProizvodAdapter.OnItemClickListener {
            override fun onItemClick(Proizvod: Trgovina) {
                val intent = Intent(this@OdabirProizvodaZaUredjivanje, UrediProizvod::class.java).apply {
                    putExtra("ProizvodKey", Proizvod.key) // Ključ proizvoda se prenosi u UrediProizvod aktivnost
                    putExtra("naziv", Proizvod.naziv)
                    putExtra("cijena", Proizvod.cijena)
                    putExtra("imageUrl", Proizvod.imageUrl)
                }
                startActivity(intent)
            }
        })
        recyclerView.adapter = ProizvodAdapter

        fetchJelaFromFirebase()
    }

    private fun fetchJelaFromFirebase() {
        val database = FirebaseDatabase.getInstance("https://useradminbaserestoran-default-rtdb.europe-west1.firebasedatabase.app/")
        val reference = database.getReference("jelo")

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val Proizvodi = mutableListOf<Trgovina>()

                for (ProizvodSnapshot in snapshot.children) {
                    val key = ProizvodSnapshot.key.toString() // Ključ jela
                    val naziv = ProizvodSnapshot.child("naziv").getValue(String::class.java) ?: ""
                    val cijena = ProizvodSnapshot.child("cijena").getValue(String::class.java) ?: ""
                    val imageUrl = ProizvodSnapshot.child("imageUrl").getValue(String::class.java) ?: ""

                    val noviProizvod = Trgovina(key, naziv, cijena, imageUrl)
                    Proizvodi.add(noviProizvod)
                }

                // Update Adapter with new data
               ProizvodAdapter.ListProizvod = Proizvodi
                ProizvodAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Error handling
                Log.e("Firebase", "Error fetching data: ${error.message}")
            }
        })
    }
}