package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF17_2Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17_2)
        enableEdgeToEdge()

        val editButton = findViewById<ImageButton>(R.id.editButton)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        editButton.setOnClickListener {
            val intent = Intent(this, TelaRF17_4Activity::class.java)
            startActivity(intent)
        }

        btnAdd.setOnClickListener {
            val intent = Intent(this, TelaRF17_3Activity::class.java)
            startActivity(intent)
        }
    }
}
