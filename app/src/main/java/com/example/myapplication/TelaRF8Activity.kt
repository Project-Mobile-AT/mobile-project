package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF8Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf8)

        val btn_personal = findViewById<Button>(R.id.btn_personal)
        btn_personal.setOnClickListener {
            val intent = Intent(this, TelaRF5_1Activity::class.java)
            startActivity(intent)
        }
    }
}