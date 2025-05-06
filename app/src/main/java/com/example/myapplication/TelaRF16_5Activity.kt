package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF16_5Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf16_5)

        val addNovoAluno = findViewById<Button>(R.id.btnAdicionarAluno)
        val visualizarTreino = findViewById<Button>(R.id.visualizar_ficha)

        addNovoAluno.setOnClickListener {
            val intent = Intent(this, TelaRF16_6Activity::class.java)
            startActivity(intent)
        }

        visualizarTreino.setOnClickListener {
            val intent = Intent(this, TelaRF12_1Activity::class.java)
            startActivity(intent)
        }
    }
}