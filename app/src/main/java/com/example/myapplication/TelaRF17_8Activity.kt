package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF17_8Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17_8)
        enableEdgeToEdge()

        val btnVoltar = findViewById<ImageView>(R.id.btn_back)

        btnVoltar.setOnClickListener {
            val intent = Intent(this, TelaRF17Activity::class.java)
            startActivity(intent)
            finish()
        }

        val btnConfirmar = findViewById<Button>(R.id.btn_confirmar)

        btnConfirmar.setOnClickListener {
            Toast.makeText(this, "Nutricionista cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, TelaRF17Activity::class.java)
            startActivity(intent)
            finish()
        }
    }
}