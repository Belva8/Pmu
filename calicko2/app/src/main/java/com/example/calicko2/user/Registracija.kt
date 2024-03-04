package com.example.user

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class Registracija : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var etIme: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registracija)

        auth = FirebaseAuth.getInstance()

        val etIme = findViewById<EditText>(R.id.etIme)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etLozinka = findViewById<EditText>(R.id.etLozinka)
        val etPotvrdiLozinku = findViewById<EditText>(R.id.etPotvrdiLozinku)
        val buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            val ime = etIme.text.toString()
            val email = etEmail.text.toString()
            val lozinka = etLozinka.text.toString()
            val potvrdiLozinku = etPotvrdiLozinku.text.toString()

            if (ime.isEmpty() || email.isEmpty() || lozinka.isEmpty() || potvrdiLozinku.isEmpty()) {
                Toast.makeText(this, "Molimo vas da unesete sve podatke", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (lozinka != potvrdiLozinku) {
                Toast.makeText(this, "Lozinke se ne podudaraju", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, lozinka)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = auth.currentUser
                        Toast.makeText(this, "Registracija uspješna: ${user?.email}", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, MainActivity::class.java)
                        intent.putExtra("USER_NAME", ime)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "Registracija nije uspjela. ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}
