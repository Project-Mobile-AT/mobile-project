package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF17_7Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17_7)
        enableEdgeToEdge()

        val backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener { val intent = Intent(this, TelaRF17_5Activity::class.java)
            startActivity(intent)}
    }
}

