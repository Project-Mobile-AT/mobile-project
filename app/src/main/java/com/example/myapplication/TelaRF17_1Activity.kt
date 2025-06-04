package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF17_1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telarf17_1)
        enableEdgeToEdge()

        // Botão voltar
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish()
        }

        // Botão para ver horários
        val btnHorarios = findViewById<Button>(R.id.btnHorarios)
        btnHorarios.setOnClickListener {
            val intent = Intent(this, TelaRF17_3Activity::class.java)
            startActivity(intent)
        }

        // Botão para adicionar horário
        val btnAdicionarHorario = findViewById<Button>(R.id.btnAdicionarHorario)
        btnAdicionarHorario.setOnClickListener {
            val intent = Intent(this, TelaRF17_2Activity::class.java)
            startActivity(intent)
        }
    }
}
