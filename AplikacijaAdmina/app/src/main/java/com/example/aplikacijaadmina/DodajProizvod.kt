package com.example.aplikacijaadmina

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.Button
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DodajProizvod : AppCompatActivity() {
    private lateinit var Dodaj : Button
    private lateinit var ETNazivProizvoda: EditText
    private lateinit var ETCijenaProizvoda: EditText
    private lateinit var imageViewUpload: ImageView
    private lateinit var selectedImageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dodavanje_proizvoda)

        ETNazivProizvoda = findViewById(R.id.ETNazivProizvoda)
        ETCijenaProizvoda = findViewById(R.id.ETCijenaProizvoda)
        imageViewUpload = findViewById(R.id.imageViewUpload)
        imageViewUpload.setOnClickListener {
            pokreniOdabirSlike()
        }

        val Dodaj: Button = findViewById(R.id.Dodaj)
        Dodaj.setOnClickListener {
            saveDataToFirebase()
        }
    }

    private fun pokreniOdabirSlike() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    private fun saveDataToFirebase() {
        val nazivJela: String = ETNazivProizvoda.text.toString()
        val cijenaJela: String = ETCijenaProizvoda.text.toString()

        val database = FirebaseDatabase.getInstance("https://useradminbaserestoran-default-rtdb.europe-west1.firebasedatabase.app/")
        val reference = database.getReference("jelo")

        val jelo = mapOf("naziv" to nazivJela, "cijena" to cijenaJela)

        val key = reference.push().key
        key?.let {
            reference.child(it).setValue(jelo)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Uspješno spremljeno
                        Log.d("Firebase", "Podaci uspješno spremljeni.")
                        val jeloKey = it
                        // Dodaj imageUrl u zapis u bazi
                        val imageUrl = "slike/$jeloKey"
                        reference.child(jeloKey).child("imageUrl").setValue(imageUrl)
                        // Pozivamo funkciju za učitavanje slike i proslijeđujemo joj ključ jela
                        uploadSlike(jeloKey)
                        clearInputFields()
                    } else {
                        val error = task.exception?.message
                        Log.e("Firebase", "Greška prilikom spremanja podataka: $error")
                    }
                }
        }
    }

    private fun uploadSlike(jeloKey: String) {
        if (::selectedImageUri.isInitialized) {
            val storage: FirebaseStorage = FirebaseStorage.getInstance("gs://useradminbaserestoran.appspot.com")
            val storageReference: StorageReference = storage.reference.child("slike/$jeloKey") // Koristimo ključ jela za ime datoteke slike
            storageReference.putFile(selectedImageUri)
                .addOnSuccessListener { taskSnapshot ->
                    // Slika je uspešno postavljena
                    Log.d("Firebase", "Slika uspešno postavljena.")

                    // Dobivamo URL slike nakon što se uspješno postavi
                    storageReference.downloadUrl.addOnSuccessListener { uri ->
                        // Ažuriramo bazu podataka s URL-om slike
                        FirebaseDatabase.getInstance().getReference("jelo/$jeloKey/imageUrl").setValue(uri.toString())
                            .addOnSuccessListener {
                                Log.d("Firebase", "URL slike uspješno spremljen.")
                            }
                            .addOnFailureListener {
                                Log.e("Firebase", "Greška prilikom spremanja URL-a slike: ${it.message}")
                            }
                    }
                }
                .addOnFailureListener { exception ->
                    // Greška prilikom postavljanja slike
                    Log.e("Firebase", "Greška prilikom postavljanja slike: ${exception.message}")
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
        ETNazivProizvoda.text.clear()
        ETCijenaProizvoda.text.clear()
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
