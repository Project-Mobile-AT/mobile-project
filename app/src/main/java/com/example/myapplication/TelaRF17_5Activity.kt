package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF17_5Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17_5)
        enableEdgeToEdge()

        val addPaciente = findViewById<LinearLayout>(R.id.addPaciente)
        val btnFichaNutricional1 = findViewById<Button>(R.id.btnFichaNutricional1)
        val btnFichaNutricional2 = findViewById<Button>(R.id.btnFichaNutricional2)

        addPaciente.setOnClickListener {
            val intent = Intent(this, TelaRF17_6Activity::class.java)
            startActivity(intent)
        }

        btnFichaNutricional1.setOnClickListener {
            val intent = Intent(this, TelaRF17_7Activity::class.java)
            startActivity(intent)
        }

        btnFichaNutricional2.setOnClickListener {
            val intent = Intent(this, TelaRF17_7Activity::class.java)
            startActivity(intent)
        }
    }
}
