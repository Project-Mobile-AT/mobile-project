package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.fragments.BottomNavFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TelaRF3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf3)

        val perfil = findViewById<ImageView>(R.id.ic_per)
        val buttonMinhaFicha = findViewById<Button>(R.id.btn_minhaFicha)
        val btn_checkin = findViewById<Button>(R.id.btn_checkin)
        val fabChat = findViewById<FloatingActionButton>(R.id.fab_chat)
        val fabAcessibilidade = findViewById<FloatingActionButton>(R.id.fab_acessibilidade)
        val imageView = findViewById<ImageView>(R.id.imageView)

        perfil.setOnClickListener {
            val intent = Intent(this, TelaRF3_1Activity::class.java)
            startActivity(intent)
        }

        buttonMinhaFicha.setOnClickListener {
            val intent = Intent(this, TelaRF5Activity::class.java)
            startActivity(intent)
        }

        btn_checkin.setOnClickListener {
            val intent = Intent(this, TelaRF5Activity::class.java)
            startActivity(intent)
        }
        fabChat.setOnClickListener {
            val intent = Intent(this, TelaRF9Activity::class.java)
            startActivity(intent)
        }
        fabAcessibilidade.setOnClickListener {
            val intent = Intent(this, TelaRF9Activity::class.java)
            startActivity(intent)
        }
        imageView.setOnClickListener {
            val intent = Intent(this, TelaRF9Activity::class.java)
            startActivity(intent)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_bottom_nav, BottomNavFragment())
            .commit()

    }
}