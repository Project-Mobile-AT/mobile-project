package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF7Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf7)

        val btn_dieta = findViewById<Button>(R.id.btn_dieta)

        btn_dieta.setOnClickListener {
            val intent = Intent(this, TelaRF7_1Activity::class.java)
            startActivity(intent)
        }
    }
}