package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF16_2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf16_2)

        val editar = findViewById<ImageView>(R.id.editar)

        editar.setOnClickListener {
            val intent = Intent(this, TelaRF16_4Activity::class.java)
            startActivity(intent)
        }

        val addHorario = findViewById<Button>(R.id.button_add_appointment)

        addHorario.setOnClickListener {
            val intent = Intent(this, TelaRF16_3Activity::class.java)
            startActivity(intent)
        }
    }
}