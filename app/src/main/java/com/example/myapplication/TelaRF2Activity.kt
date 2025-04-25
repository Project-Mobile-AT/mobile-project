package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.textclassifier.TextLinks.TextLink
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf2)

        val link_login = findViewById<TextView>(R.id.link_login)
        val button = findViewById<Button>(R.id.button)

        link_login.setOnClickListener {
            val intent = Intent (this, TelaRF1Activity::class.java)
            startActivity(intent)
        }

        button.setOnClickListener {
            val intent = Intent (this, TelaRF2_1Activity::class.java)
            startActivity(intent)
        }

    }
}