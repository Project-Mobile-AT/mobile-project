package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF17_6Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf17_6)

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)

        backButton.setOnClickListener {
            finish()
        }

        btnConfirmar.setOnClickListener {
            // TODO: Implementar lógica de confirmação do agendamento
            finish()
        }
    }
}