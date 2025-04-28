package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.fragments.ChatbotPopupFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TelaRF19_2Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf19_2)

        val inicio = findViewById<ImageView>(R.id.homeIcon)

        val voltar = findViewById<ImageView>(R.id.btVoltar)

        val fabChat = findViewById<FloatingActionButton>(R.id.fab_chat)

        inicio.setOnClickListener {
            val intent = Intent(this, TelaRF11Activity::class.java)
            startActivity(intent)
        }

        voltar.setOnClickListener {
            val intent = Intent(this, TelaRF19Activity::class.java)
            startActivity(intent)
        }

        fabChat.setOnClickListener {
            val chatbotPopup = ChatbotPopupFragment()
            chatbotPopup.show(supportFragmentManager, "ChatbotPopup")
        }

    }
}