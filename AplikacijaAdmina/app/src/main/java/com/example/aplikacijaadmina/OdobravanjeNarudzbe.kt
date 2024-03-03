package com.example.aplikacijaadmina

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import android.util.Log
import com.google.firebase.database.FirebaseDatabase


class OdobravanjeNarudzbe : AppCompatActivity() {

    private lateinit var zahtjeviReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.odobravanje_narudzbe)

        val databaseReference = FirebaseDatabase.getInstance("https://useradminbaserestoran-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
        zahtjeviReference = databaseReference.child("zahtjevi")

        val email = intent.getStringExtra("email") ?: ""

        val Odobravam: Button = findViewById(R.id.Odobravam)
        val Odbijam: Button = findViewById(R.id.Odbijam)

        Odobravam.setOnClickListener {
            updateStatus(email, 1)
        }

        Odbijam.setOnClickListener {
            updateStatus(email, -1)
        }
    }

    private fun updateStatus(email: String, status: Int) {
        zahtjeviReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (zahtjevSnapshot in snapshot.children) {
                    zahtjevSnapshot.child("odobren").getRef().setValue(status)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error updating data: ${error.message}")
            }
        })

        finish()
    }
}