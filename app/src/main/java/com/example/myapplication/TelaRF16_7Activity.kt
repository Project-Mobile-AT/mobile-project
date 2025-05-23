package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF16_7Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf16_7)
        enableEdgeToEdge()

        val btnVoltar = findViewById<ImageView>(R.id.btn_back)

        val btnConfirmar = findViewById<Button>(R.id.btn_confirmar)

        btnVoltar.setOnClickListener {
            val intent = Intent(this, TelaRF16_5Activity::class.java)
            startActivity(intent)
            finish()
        }

        btnConfirmar.setOnClickListener {
            Toast.makeText(this, "Personal cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TelaRF16_5Activity::class.java)
            startActivity(intent)
            finish()
        }
    }
}