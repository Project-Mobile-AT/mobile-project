package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class TelaRF17_2Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17_2)
        enableEdgeToEdge()

        val editButton1 = findViewById<ImageButton>(R.id.editButton1)
        val editButton2 = findViewById<ImageButton>(R.id.editButton2)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        // Set listener for the first edit button
        editButton1.setOnClickListener {
            val intent = Intent(this, TelaRF17_4Activity::class.java)
            startActivity(intent)
        }

        // Set listener for the second edit button
        editButton2.setOnClickListener {
            val intent = Intent(this, TelaRF17_4Activity::class.java)
            startActivity(intent)
        }

        btnAdd.setOnClickListener {
            val intent = Intent(this, TelaRF17_3Activity::class.java)
            startActivity(intent)
        }
    }
}