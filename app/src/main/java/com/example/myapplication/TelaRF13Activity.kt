package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF13Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf13)

        val addEquipamento = findViewById<Button>(R.id.btnAdicionarEquipamento)
        val editarEquipamento = findViewById<ImageButton>(R.id.edit_equipamento)

        addEquipamento.setOnClickListener {
            val intent = Intent(this, TelaRF13_2Activity::class.java)
            startActivity(intent)
        }

        editarEquipamento.setOnClickListener {
            val intent = Intent(this, TelaRF13_1Activity::class.java)
            startActivity(intent)
        }

    }
}