package com.example.user

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        val tvClickableRegistracija = findViewById<TextView>(R.id.tvClickableRegistracija)

        tvClickableRegistracija.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@UserActivity, Registracija::class.java)
            startActivity(intent)
        })
    }
}
