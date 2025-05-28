package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class TelaRF1Activity : AppCompatActivity() {

    lateinit var fb : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tela_rf1)

        val btnLogin = findViewById<Button>(R.id.btn_login)
        val recuperarSenha = findViewById<TextView>(R.id.tv_esqueceu_senha)
        val criarConta = findViewById<TextView>(R.id.tv_registre_se)
        val etEmail = findViewById<EditText>(R.id.et_email)
         fb = Firebase.firestore
        recuperarSenha.paint.isUnderlineText = true
        criarConta.paint.isUnderlineText = true


       // fb.collection("")
        btnLogin.setOnClickListener {
//            fb.collection("usuario").get().addOnSuccessListener {
//                docs ->
//                for (doc in docs){
//                    Log.d("banco",doc.get("email").toString())
//                    Log.d("banco", doc.id)
//                }


            }

            val emailInput = etEmail.text.toString().trim()

            if (emailInput.equals("admin", ignoreCase = true)) {
                val intent = Intent(this, TelaRF11Activity::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this, TelaRF3Activity::class.java) // Tela padrão do usuário
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
