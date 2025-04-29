package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF17Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17)
        enableEdgeToEdge()

        val nutriNome = findViewById<LinearLayout>(R.id.nutriNome)

        nutriNome.setOnClickListener {
            val intent = Intent(this, TelaRF17_1Activity::class.java)
            startActivity(intent)
        }
    }
}