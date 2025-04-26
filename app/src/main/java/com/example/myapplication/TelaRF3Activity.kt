package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.fragments.BottomNavFragment

class TelaRF3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf3)

        val perfil = findViewById<ImageView>(R.id.ic_per)

        perfil.setOnClickListener {
            val intent = Intent(this, TelaRF3_1Activity::class.java)
            startActivity(intent)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_bottom_nav, BottomNavFragment())
            .commit()

    }
}