package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.Equipamento
import kotlinx.coroutines.launch
import java.util.UUID

class TelaRF13_2Activity : AppCompatActivity() {
    private lateinit var etNome: EditText
    private lateinit var etDescricao: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnVoltar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf13_2)

        inicializarViews()
        configurarListeners()
    }

    private fun inicializarViews() {
        etNome = findViewById(R.id.etNomeEquipamento)
        etDescricao = findViewById(R.id.etDescricaoEquipamento)
        btnSalvar = findViewById(R.id.btnSalvarEquipamento)
        btnVoltar = findViewById(R.id.btnVoltarEquipamento)
    }

    private fun configurarListeners() {
        btnSalvar.setOnClickListener {
            salvarEquipamento()
        }

        btnVoltar.setOnClickListener {
            finish()
        }
    }

    private fun salvarEquipamento() {
        val nome = etNome.text.toString().trim()
        val descricao = etDescricao.text.toString().trim()

        if (nome.isEmpty()) {
            Toast.makeText(this, "Preencha o nome do equipamento", Toast.LENGTH_SHORT).show()
            return
        }

        val novoEquipamento = Equipamento(
            id = UUID.randomUUID().toString(),
            nome = nome,
            descricao = descricao.ifEmpty { null }
        )

        lifecycleScope.launch {
            try {
                val response = SupabaseClient.service.criarEquipamento(novoEquipamento)
                if (response.isNotEmpty()) {
                    Toast.makeText(this@TelaRF13_2Activity, 
                        "Equipamento cadastrado com sucesso!", 
                        Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@TelaRF13_2Activity, 
                        "Erro ao cadastrar equipamento", 
                        Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TelaRF13_2Activity, 
                    "Erro ao cadastrar: ${e.message}", 
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}