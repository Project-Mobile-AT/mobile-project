package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.Exercicio
import com.example.myapplication.model.Equipamento
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TelaRF12_1Activity : AppCompatActivity() {
    private var listaEquipamentos: List<Equipamento> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf12_1)
        enableEdgeToEdge()

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val editNomeExercicio = findViewById<EditText>(R.id.editNomeExercicio)
        val editDescricaoExercicio = findViewById<EditText>(R.id.editDescricaoExercicio)
        val spinnerEquipamento = findViewById<Spinner>(R.id.spinnerEquipamento1)

        btnVoltar.setOnClickListener {
            val intent = Intent(this, TelaRF11Activity::class.java)
            startActivity(intent)
        }

        carregarEquipamentos(spinnerEquipamento)

        btnSalvar.setOnClickListener {
            val nome = editNomeExercicio.text.toString().trim()
            val descricao = editDescricaoExercicio.text.toString().trim()
            val pos = spinnerEquipamento.selectedItemPosition
            val equipamentoId = if (pos >= 0 && pos < listaEquipamentos.size) listaEquipamentos[pos].id else null

            if (nome.isEmpty()) {
                Toast.makeText(this, "Digite o nome do exercício", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (equipamentoId.isNullOrEmpty()) {
                Toast.makeText(this, "Selecione um equipamento válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val novoExercicio = Exercicio(
                        id = null,
                        nome = nome,
                        descricao = descricao,
                        equipamento_id = equipamentoId
                    )
                    SupabaseClient.service.criarExercicio(novoExercicio)
                    runOnUiThread {
                        Toast.makeText(this@TelaRF12_1Activity, "Exercício cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@TelaRF12_1Activity, "Erro ao cadastrar: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun carregarEquipamentos(spinner: Spinner) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val equipamentos = SupabaseClient.service.getEquipamentos()
                listaEquipamentos = equipamentos
                val nomes = equipamentos.map { it.nome }
                runOnUiThread {
                    val adapter = ArrayAdapter(this@TelaRF12_1Activity, android.R.layout.simple_spinner_item, nomes)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@TelaRF12_1Activity, "Erro ao carregar equipamentos", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}