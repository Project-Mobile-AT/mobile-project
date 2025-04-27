package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF5Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf5)


        val perfil = findViewById<ImageView>(R.id.ic_per)


        val btn_consulta = findViewById<Button>(R.id.btn_consulta)

        btn_consulta.setOnClickListener {
            val intent = Intent(this, TelaRF5_1Activity::class.java)
            startActivity(intent)
        }

        perfil.setOnClickListener {
            val intent = Intent(this, TelaRF3_1Activity::class.java)
            startActivity(intent)
        }
    }
}