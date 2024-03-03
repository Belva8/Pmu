package com.example.aplikacijaadmina

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class UrediProizvod : AppCompatActivity() {
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.uredi_proizvod)

        // Initialize Firebase database reference
        databaseReference = FirebaseDatabase.getInstance("https://useradminbaserestoran-default-rtdb.europe-west1.firebasedatabase.app/").getReference("jelo")

        // Retrieve data passed from previous activity
        val ProizvodKey = intent.getStringExtra("ProizvodKey")
        val nazivProizvoda = intent.getStringExtra("naziv")
        val cijenaProizvoda = intent.getStringExtra("cijena")
        val imageUrl = intent.getStringExtra("imageUrl")

        val editTextNazivProizvoda = findViewById<EditText>(R.id.editTextNazivProizvoda)
        val editTextCijenaProizvoda = findViewById<EditText>(R.id.editTextCijenaProizvoda)
        val imageViewUpload = findViewById<ImageView>(R.id.imageViewUpload)
        val btnUredi = findViewById<Button>(R.id.btnUredi)

        editTextNazivProizvoda.setText(nazivProizvoda)
        editTextCijenaProizvoda.setText(cijenaProizvoda)

        btnUredi.setOnClickListener {
            val newNaziv = editTextNazivProizvoda.text.toString()
            val newCijena = editTextCijenaProizvoda.text.toString()

            if (newNaziv.isNotEmpty() && newCijena.isNotEmpty()) {
                // Create a map with the updated data
                val updatedData = mapOf(
                    "naziv" to newNaziv,
                    "cijena" to newCijena,
                    "imageUrl" to imageUrl // If you want to update image URL as well, otherwise, remove this line
                )

                // Update the corresponding node in Firebase database using the received key (jeloKey)
                databaseReference.child(ProizvodKey!!).setValue(updatedData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Changes saved successfully!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to save changes: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "Please enter all information.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
