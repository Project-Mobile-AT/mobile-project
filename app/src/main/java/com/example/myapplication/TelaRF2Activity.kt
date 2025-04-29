package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.TelaRF1Activity
import com.example.myapplication.TelaRF3Activity
import com.example.myapplication.R

class TelaRF2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf2)

        // Inicializando os componentes de UI
        val btnEntrar = findViewById<Button>(R.id.button)
        val linkLogin = findViewById<TextView>(R.id.link_login)

        // Ação para o botão "Entrar"
        btnEntrar.setOnClickListener {
            // Navegar para a tela principal (ou qualquer outra tela desejada)
            val intent = Intent(this, TelaRF2_1Activity::class.java)
            startActivity(intent)
        }

        // Ação para o link de login
        linkLogin.setOnClickListener {
            // Navegar para a tela de login
            val intent = Intent(this, TelaRF1Activity::class.java)
            startActivity(intent)
        }
    }
}
