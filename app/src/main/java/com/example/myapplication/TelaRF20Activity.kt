package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class TelaRF20Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf20)

        val homeIcon: ImageView = findViewById(R.id.homeIcon)

        homeIcon.setOnClickListener {
            val intent = Intent(this, TelaRF11Activity::class.java)
            startActivity(intent)
        }
    }
}
