package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF17_8Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17_8)
        enableEdgeToEdge()

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val btnCancelar = findViewById<Button>(R.id.btnCancelar)
        val btnReagendar = findViewById<Button>(R.id.btnReagendar)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val tvTime = findViewById<TextView>(R.id.tvTime)
        val tvReason = findViewById<TextView>(R.id.tvReason)
        val tvNotes = findViewById<TextView>(R.id.tvNotes)

        // TODO: Receber dados do agendamento via intent e preencher os campos

        backButton.setOnClickListener {
            finish()
        }

        btnCancelar.setOnClickListener {
            // TODO: Implementar lógica de cancelamento
            finish()
        }

        btnReagendar.setOnClickListener {
            // TODO: Implementar lógica de reagendamento
            finish()
        }
    }
}