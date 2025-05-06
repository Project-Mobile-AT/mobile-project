package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF12Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf12)
        enableEdgeToEdge()

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)

        btnVoltar.setOnClickListener {
            val intent = Intent(this, TelaRF11Activity::class.java)
            startActivity(intent)
        }

        val AdicionarExercicio = findViewById<Button>(R.id.btnAdicionarExercicio)

        AdicionarExercicio.setOnClickListener {
            val intent = Intent(this, TelaRF12_1Activity::class.java)
            startActivity(intent)
        }

        val btnEdit1 = findViewById<ImageButton>(R.id.btnEdit1)

        btnEdit1.setOnClickListener {
            val intent = Intent(this, TelaRF12_1Activity::class.java)
            startActivity(intent)
        }

        val btnEdit2 = findViewById<ImageButton>(R.id.btnEdit2)

        btnEdit2.setOnClickListener {
            val intent = Intent(this, TelaRF12_1Activity::class.java)
            startActivity(intent)
        }

        val btnEdit3 = findViewById<ImageButton>(R.id.btnEdit3)

        btnEdit3.setOnClickListener {
            val intent = Intent(this, TelaRF12_1Activity::class.java)
            startActivity(intent)
        }
        val btnAdicionarExercicio = findViewById<Button>(R.id.btnAdicionarExercicio)

        btnAdicionarExercicio.setOnClickListener {
            val intent = Intent(this, TelaRF12_1Activity::class.java)
            startActivity(intent)
        }
    }
}