package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TelaRF17_7Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17_7)
        enableEdgeToEdge()

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        // TODO: Implementar adapter para o histórico de agendamentos

        backButton.setOnClickListener {
            finish()
        }
    }
}

