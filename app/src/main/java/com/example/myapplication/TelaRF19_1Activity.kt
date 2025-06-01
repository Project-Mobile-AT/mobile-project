package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.AtualizacaoInformativo
import kotlinx.coroutines.launch

class TelaRF19_1Activity : AppCompatActivity() {

    private lateinit var infoEditText: EditText
    private lateinit var etTitulo: EditText
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button
    private lateinit var informativoId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf19_1)

        etTitulo = findViewById(R.id.etTitulo)
        infoEditText = findViewById(R.id.infoEditText)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)

        val idRecebido = intent.getStringExtra("INFORMATIVO_ID")
        val tituloRecebido = intent.getStringExtra("INFORMATIVO_TITULO") ?: ""
        val conteudoRecebido = intent.getStringExtra("INFORMATIVO_CONTEUDO") ?: ""

        if (idRecebido.isNullOrBlank()) {
            Toast.makeText(this, "ID do informativo não recebido.", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        informativoId = idRecebido

        etTitulo.setText(tituloRecebido)
        infoEditText.setText(conteudoRecebido)

        findViewById<ImageView>(R.id.btVoltar).setOnClickListener { finish() }

        saveButton.setOnClickListener {
            val novoTitulo = etTitulo.text.toString()
            val novoConteudo = infoEditText.text.toString()

            if (novoTitulo.isNotBlank() && novoConteudo.isNotBlank()) {
                val atualizacao = AtualizacaoInformativo(
                    titulo = novoTitulo,
                    conteudo = novoConteudo
                )

                Log.d("DEBUG_HTTP", "ID = $informativoId")
                Log.d("DEBUG_HTTP", "Atualizando com: $atualizacao")

                lifecycleScope.launch {
                    try {
                        SupabaseClient.service.atualizarInformativo("eq.$informativoId", atualizacao)
                        Toast.makeText(this@TelaRF19_1Activity, "Atualizado com sucesso", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } catch (e: Exception) {
                        Log.e("PATCH_ERROR", "Erro ao atualizar: ${e.message}", e)
                        Toast.makeText(this@TelaRF19_1Activity, "Erro ao atualizar (400): ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            Log.d("DEBUG_HTTP", "Tentando deletar ID = $informativoId")
            lifecycleScope.launch {
                try {
                    SupabaseClient.service.deletarInformativo("eq.$informativoId")
                    Toast.makeText(this@TelaRF19_1Activity, "Informativo deletado", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } catch (e: Exception) {
                    Log.e("DELETE_ERROR", "Erro ao deletar: ${e.message}", e)
                    Toast.makeText(this@TelaRF19_1Activity, "Erro ao deletar (400): ${e.message}", Toast.LENGTH_LONG).show()
                    setResult(RESULT_OK)
                    finish()
                }
            }
        }
    }
}
