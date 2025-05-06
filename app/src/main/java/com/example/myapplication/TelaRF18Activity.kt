package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF18Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf18)

        val editarAlunos = findViewById<ImageView>(R.id.editAlunos)

        editarAlunos.setOnClickListener {
            val intent = Intent(this, TelaRF18_1Activity::class.java)
            startActivity(intent)
        }

        val nomeAluno = findViewById<LinearLayout>(R.id.aluno)

        nomeAluno.setOnClickListener {
            val intent = Intent(this, TelaRF18_2Activity::class.java)
            startActivity(intent)
        }
    }
}