package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF16_1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf16_1)

        val visualizarTreino = findViewById<Button>(R.id.btn_view_record_ana)

        visualizarTreino.setOnClickListener {
            val intent = Intent(this, TelaRF12_1Activity::class.java)
            startActivity(intent)
        }

        val visualizarHorarios = findViewById<Button>(R.id.btn_schedule)

        visualizarHorarios.setOnClickListener {
            val intent = Intent(this, TelaRF16_2Activity::class.java)
            startActivity(intent)
        }

        val addAluno = findViewById<Button>(R.id.btn_add_student)

        addAluno.setOnClickListener {
            val intent = Intent(this, TelaRF16_5Activity::class.java)
            startActivity(intent)
        }
    }
}