package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.Equipamento
import kotlinx.coroutines.launch

class TelaRF13_1Activity : AppCompatActivity() {
    private lateinit var etNome: EditText
    private lateinit var etDescricao: EditText
    private lateinit var btnEditar: Button
    private lateinit var btnDeletar: Button
    private lateinit var btnVoltar: ImageButton
    private var equipamentoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf13_1)

        equipamentoId = intent.getStringExtra("equipamento_id")
        if (equipamentoId == null) {
            Toast.makeText(this, "Erro ao carregar equipamento", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        inicializarViews()
        configurarListeners()
    }

    private fun inicializarViews() {
        etNome = findViewById(R.id.etNome)
        etDescricao = findViewById(R.id.etDescricao)
        btnEditar = findViewById(R.id.btnEditar)
        btnDeletar = findViewById(R.id.btnDeletar)
        btnVoltar = findViewById(R.id.backButton)

        // Preencher os campos com os dados recebidos
        etNome.setText(intent.getStringExtra("equipamento_nome"))
        etDescricao.setText(intent.getStringExtra("equipamento_descricao"))
    }

    private fun configurarListeners() {
        btnVoltar.setOnClickListener {
            finish()
        }

        btnEditar.setOnClickListener {
            editarEquipamento()
        }

        btnDeletar.setOnClickListener {
            deletarEquipamento()
        }
    }

    private fun editarEquipamento() {
        val nome = etNome.text.toString().trim()
        val descricao = etDescricao.text.toString().trim()

        if (nome.isEmpty()) {
            Toast.makeText(this, "Preencha o nome do equipamento", Toast.LENGTH_SHORT).show()
            return
        }

        val equipamentoAtualizado = Equipamento(
            id = equipamentoId!!,
            nome = nome,
            descricao = descricao.ifEmpty { null }
        )

        Log.d("DEBUG_HTTP", "ID = $equipamentoId")
        Log.d("DEBUG_HTTP", "Atualizando com: $equipamentoAtualizado")

        lifecycleScope.launch {
            try {
                val response = SupabaseClient.service.atualizarEquipamento(
                    id = "eq.$equipamentoId",
                    equipamento = equipamentoAtualizado
                )
                if (response.isNotEmpty()) {
                    Toast.makeText(this@TelaRF13_1Activity, 
                        "Equipamento atualizado com sucesso!", 
                        Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@TelaRF13_1Activity, 
                        "Erro ao atualizar equipamento", 
                        Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("PATCH_ERROR", "Erro ao atualizar: ${e.message}", e)
                Toast.makeText(this@TelaRF13_1Activity, 
                    "Erro ao atualizar: ${e.message}", 
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deletarEquipamento() {
        Log.d("DEBUG_HTTP", "Tentando deletar ID = $equipamentoId")
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.service.deletarEquipamento("eq.$equipamentoId")
                if (response.isSuccessful) {
                    Toast.makeText(this@TelaRF13_1Activity, 
                        "Equipamento excluído com sucesso!", 
                        Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                } else {
                    Toast.makeText(this@TelaRF13_1Activity, 
                        "Erro ao excluir equipamento", 
                        Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Log.e("DELETE_ERROR", "Erro ao deletar: ${e.message}", e)
                Toast.makeText(this@TelaRF13_1Activity, 
                    "Erro ao excluir: ${e.message}", 
                    Toast.LENGTH_LONG).show()
            }
        }
    }
}