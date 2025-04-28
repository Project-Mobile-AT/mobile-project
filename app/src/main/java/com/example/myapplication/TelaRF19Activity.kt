package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.fragments.ChatbotPopupFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TelaRF19Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf19)

        val addInform = findViewById<Button>(R.id.btnAdicionarInformativo)

        val editInform = findViewById<ImageButton>(R.id.btnEditInform)

        val fabChat = findViewById<FloatingActionButton>(R.id.fab_chat)

        val inicio = findViewById<ImageView>(R.id.homeIcon)

        addInform.setOnClickListener {
            val intent = Intent(this, TelaRF19_2Activity::class.java)
            startActivity(intent)
        }

        editInform.setOnClickListener {
            val intent = Intent(this, TelaRF19_1Activity::class.java)
            startActivity(intent)
        }

        inicio.setOnClickListener {
            val intent = Intent(this, TelaRF11Activity::class.java)
            startActivity(intent)
        }

        fabChat.setOnClickListener {
            val chatbotPopup = ChatbotPopupFragment()
            chatbotPopup.show(supportFragmentManager, "ChatbotPopup")
        }

    }
}