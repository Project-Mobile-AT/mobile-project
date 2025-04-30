package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TelaRF1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tela_rf1)

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val recuperarSenha = findViewById<TextView>(R.id.tv_esqueceu_senha)
        val criarConta = findViewById<TextView>(R.id.tv_registre_se)
        val etEmail = findViewById<EditText>(R.id.et_email)

        recuperarSenha.paint.isUnderlineText = true
        criarConta.paint.isUnderlineText = true

        btnLogin.setOnClickListener {
            val emailInput = etEmail.text.toString().trim()

            if (emailInput.equals("admin", ignoreCase = true)) {
                val intent = Intent(this, TelaRF11Activity::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this, TelaRF3Activity::class.java) // Tela padrão do usuário
                startActivity(intent)
            }
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