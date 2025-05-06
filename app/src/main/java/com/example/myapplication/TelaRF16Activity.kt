package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF16Activity : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf16)

        val instrutor = findViewById<LinearLayout>(R.id.instrutor)
        val btn_cadastrar_personal = findViewById<Button>(R.id.btn_cadastrar_personal)

        instrutor.setOnClickListener {
            val intent = Intent(this, TelaRF16_1Activity::class.java)
            startActivity(intent)
        }

        btn_cadastrar_personal.setOnClickListener {
            val intent = Intent(this, TelaRF16_7Activity::class.java)
            startActivity(intent)
        }
    }
}