package com.example.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.*

class DodajRacun : AppCompatActivity() {
    private lateinit var editTextBrojRacuna: EditText
    private lateinit var editTextStanjeRacuna: EditText
    private lateinit var imageViewUpload: ImageView
    private lateinit var selectedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dodaj_racun)

        editTextBrojRacuna = findViewById(R.id.editTextBrojRacuna)
        editTextStanjeRacuna = findViewById(R.id.editTextStanjeRacuna)
        imageViewUpload = findViewById(R.id.imageViewUpload)
        imageViewUpload.setOnClickListener {
            pokreniOdabirSlike()
        }

        val btnDodaj: Button = findViewById(R.id.btnDodaj)
        btnDodaj.setOnClickListener {
            saveDataToFirebase()
            // Dodajte kod za dodavanje informacija o jelu u bazu podataka ili druge radnje
        }
    }

    private fun pokreniOdabirSlike() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun saveDataToFirebase() {
        val brojRacuna: String = editTextNazivJela.text.toString()
        val stanjeRacuna: String = editTextCijenaJela.text.toString()

        val database = FirebaseDatabase.getInstance("https://racun-b1e0f-default-rtdb.europe-west1.firebasedatabase.app/")
        val reference = database.getReference("racun")

        val racun = mapOf("broj" to brojRacuna, "stanje" to stanjeRacuna)

        val key = reference.push().key
        key?.let {
            reference.child(it).setValue(racun)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Uspješno spremljeno
                        Log.d("Firebase", "Podaci uspješno spremljeni.")
                        uploadSlike(it) // Dodajte poziv funkcije za upload slike
                        clearInputFields()
                    } else {
                        val error = task.exception?.message
                        Log.e("Firebase", "Greška prilikom spremanja podataka: $error")
                    }
                }
        }
    }

    private fun uploadSlike(racunKey: String) {
        if (::selectedImageUri.isInitialized) {
            val storage: FirebaseStorage = FirebaseStorage.getInstance()
            val storageReference: StorageReference = storage.reference.child("slike/" + racunKey)
            storageReference.putFile(selectedImageUri)
                .addOnSuccessListener {
                    // Slika je uspešno postavljena
                    Log.d("Firebase", "Slika uspešno postavljena.")
                }
                .addOnFailureListener {
                    // Greška prilikom postavljanja slike
                    Log.e("Firebase", "Greška prilikom postavljanja slike: ${it.message}")
                }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            selectedImageUri = data.data!!
            imageViewUpload.setImageURI(selectedImageUri)
        }
    }

    private fun clearInputFields() {
        editTextBrojRacuna.text.clear()
        editTextStanjeRacuna.text.clear()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
