package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF11Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf11)

        val equipamentos = findViewById<LinearLayout>(R.id.equipamento)
        val aulas = findViewById<LinearLayout>(R.id.aulas)
        val cadastrarAluno = findViewById<LinearLayout>(R.id.cadastrar_aluno)
        val atendNutricional = findViewById<LinearLayout>(R.id.at_nutricional)
        val atendPersonal = findViewById<LinearLayout>(R.id.at_personal)
        val alunos = findViewById<LinearLayout>(R.id.alunos)
        val informativos = findViewById<LinearLayout>(R.id.informativos)
        val cadastrarTreino = findViewById<LinearLayout>(R.id.cadastrar_treino)

        equipamentos.setOnClickListener {
            val intent = Intent(this, TelaRF13Activity::class.java)
            startActivity(intent)
        }
        aulas.setOnClickListener {
            val intent = Intent(this, TelaRF14Activity::class.java)
            startActivity(intent)
        }
        cadastrarAluno.setOnClickListener {
            val intent = Intent(this, TelaRf15Activity::class.java)
            startActivity(intent)
        }
        atendNutricional.setOnClickListener {
            val intent = Intent(this, TelaRF17Activity::class.java)
            startActivity(intent)
        }
        atendPersonal.setOnClickListener {
            val intent = Intent(this, TelaRF16Activity::class.java)
            startActivity(intent)
        }
        alunos.setOnClickListener {
            val intent = Intent(this, TelaRF18Activity::class.java)
            startActivity(intent)
        }
        informativos.setOnClickListener {
            val intent = Intent(this, TelaRF19Activity::class.java)
            startActivity(intent)
        }
        cadastrarTreino.setOnClickListener {
            // Supondo que você tenha uma atividade para cadastrar treino
            val intent = Intent(this, TelaRF12Activity::class.java)
            startActivity(intent)
        }
    }
}