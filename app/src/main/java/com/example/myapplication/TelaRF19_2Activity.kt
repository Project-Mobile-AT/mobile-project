package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.fragments.ChatbotPopupFragment
import com.example.myapplication.model.Informativo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TelaRF19_2Activity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf19_2)

        val etTitulo = findViewById<EditText>(R.id.etTitulo)
        val etConteudo = findViewById<EditText>(R.id.etInformativo)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        val inicio = findViewById<ImageView>(R.id.homeIcon)
        val voltar = findViewById<ImageView>(R.id.btVoltar)
        val fabChat = findViewById<FloatingActionButton>(R.id.fab_chat)

        inicio.setOnClickListener {
            startActivity(Intent(this, TelaRF11Activity::class.java))
        }

        voltar.setOnClickListener {
            startActivity(Intent(this, TelaRF19Activity::class.java))
        }

        fabChat.setOnClickListener {
            val chatbotPopup = ChatbotPopupFragment()
            chatbotPopup.show(supportFragmentManager, "ChatbotPopup")
        }

        btnSalvar.setOnClickListener {
            val titulo = etTitulo.text.toString().trim()
            val conteudo = etConteudo.text.toString().trim()
            val criadoEm = getDataAtualFormatada()

            if (titulo.isNotEmpty() && conteudo.isNotEmpty()) {
                val novoInformativo = Informativo(
                    id = UUID.randomUUID().toString(), // o Supabase gera automaticamente
                    titulo = titulo,
                    conteudo = conteudo,
                    criado_em = criadoEm
                )

                lifecycleScope.launch {
                    try {
                        val response = SupabaseClient.service.criarInformativo(novoInformativo)
                        Toast.makeText(this@TelaRF19_2Activity, "Informativo salvo com sucesso!", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish() // fecha a tela após salvar

                    } catch (e: Exception) {
                        Toast.makeText(this@TelaRF19_2Activity, "Erro ao salvar: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getDataAtualFormatada(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        return sdf.format(Date())
    }


}
