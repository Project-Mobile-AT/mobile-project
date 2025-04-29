package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity


class TelaRF2_1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf2_1)

        val btn_entrar = findViewById<Button>(R.id.btn_entrar)

        btn_entrar.setOnClickListener {
            val intent = Intent (this, TelaRF1Activity::class.java)
            startActivity(intent)
        }

    }
}