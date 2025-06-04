package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF17Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17)
        enableEdgeToEdge()

        // Botão para adicionar horário
        val btnAdicionarHorario = findViewById<Button>(R.id.btnAdicionarHorario)
        btnAdicionarHorario.setOnClickListener {
            val intent = Intent(this, TelaRF17_2Activity::class.java)
            startActivity(intent)
        }

        // Botão para ver horários
        val btnVerHorarios = findViewById<Button>(R.id.btnVerHorarios)
        btnVerHorarios.setOnClickListener {
            val intent = Intent(this, TelaRF17_3Activity::class.java)
            startActivity(intent)
        }

        // Botão para consultar paciente
        val btnConsultarPaciente = findViewById<Button>(R.id.btnConsultarPaciente)
        btnConsultarPaciente.setOnClickListener {
            val intent = Intent(this, ListaAlunosActivity::class.java)
            startActivity(intent)
        }
    }
}