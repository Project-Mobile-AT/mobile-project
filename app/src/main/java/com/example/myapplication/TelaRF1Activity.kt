package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.fragments.BottomNavFragment

class TelaRF1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tela_rf1)

        val btn_login = findViewById<Button>(R.id.btn_login)
        val recuperarSenha = findViewById<TextView>(R.id.tv_esqueceu_senha)
        recuperarSenha.paint.isUnderlineText = true // Essa parte não é necessária foi apenas um teste
        val criarConta = findViewById<TextView>(R.id.tv_registre_se)
        criarConta.paint.isUnderlineText = true // Essa parte não é necessária foi apenas um teste

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