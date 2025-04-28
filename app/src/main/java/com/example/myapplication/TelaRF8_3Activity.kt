package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF8_3Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf8_3)
        enableEdgeToEdge()

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        val btnReservas = findViewById<LinearLayout>(R.id.btnReservas)
        val btnInformativo = findViewById<LinearLayout>(R.id.btnInformativo)

        btnVoltar.setOnClickListener {
            val intent = Intent(this, TelaRF8_1Activity::class.java)
            startActivity(intent)
        }

        btnReservas.setOnClickListener {
            val intent = Intent(this, TelaRF8_5Activity::class.java)
            startActivity(intent)
        }

        btnInformativo.setOnClickListener {
            val intent = Intent(this, TelaRF8_4Activity::class.java)
            startActivity(intent)
        }
    }
}
