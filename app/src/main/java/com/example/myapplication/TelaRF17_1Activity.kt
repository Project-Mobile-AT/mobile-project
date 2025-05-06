package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF17_1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telarf17_1)
        enableEdgeToEdge()

        val btnPacientes = findViewById<Button>(R.id.btnPacientes)
        val btnAdicionarPaciente = findViewById<Button>(R.id.btnAdicionarPaciente)
        val btnHorarios = findViewById<Button>(R.id.btnHorarios)

        btnPacientes.setOnClickListener {
            val intent = Intent(this, TelaRF17_5Activity::class.java)
            startActivity(intent)
        }

        btnAdicionarPaciente.setOnClickListener {
            val intent = Intent(this, TelaRF17_6Activity::class.java)
            startActivity(intent)
        }

        btnHorarios.setOnClickListener {
            val intent = Intent(this, TelaRF17_2Activity::class.java)
            startActivity(intent)
        }
    }
}
