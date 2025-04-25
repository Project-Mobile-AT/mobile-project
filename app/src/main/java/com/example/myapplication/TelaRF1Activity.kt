package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TelaRF1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tela_rf1)

        val btn_login = findViewById<Button>(R.id.btn_login)
        val recuperarSenha = findViewById<TextView>(R.id.tv_esqueceu_senha)
        val criarConta = findViewById<TextView>(R.id.tv_registre_se)

        btn_login.setOnClickListener {
            val intent = Intent(this, TelaRF3Activity::class.java)
            startActivity(intent)
        }

        recuperarSenha.setOnClickListener {
            val intent = Intent(this, TelaRF1_1Activity::class.java)
            startActivity(intent)
        }

        criarConta.setOnClickListener {
            val intent = Intent(this, TelaRF2Activity::class.java)
            startActivity(intent)
        }
    }

}