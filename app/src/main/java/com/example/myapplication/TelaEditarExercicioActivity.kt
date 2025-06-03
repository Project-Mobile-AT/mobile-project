package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.Equipamento
import com.example.myapplication.model.Exercicio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TelaEditarExercicioActivity : AppCompatActivity() {
    private var listaEquipamentos: List<Equipamento> = emptyList()
    private var exercicioId: String? = null
    private var exercicioOriginal: Exercicio? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_exercicio)
        enableEdgeToEdge()

        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)
        val editNomeExercicio = findViewById<EditText>(R.id.editNomeExercicio)
        val editDescricaoExercicio = findViewById<EditText>(R.id.editDescricaoExercicio)
        val spinnerEquipamento = findViewById<Spinner>(R.id.spinnerEquipamento1)

        btnVoltar.setOnClickListener { finish() }

        exercicioId = intent.getStringExtra("exercicio_id")
        if (exercicioId.isNullOrEmpty()) {
            Toast.makeText(this, "Exercício inválido", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        carregarEquipamentos(spinnerEquipamento) {
            carregarExercicio(editNomeExercicio, editDescricaoExercicio, spinnerEquipamento)
        }

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
                    val exercicioEditado = Exercicio(
                        id = exercicioId,
                        nome = nome,
                        descricao = descricao,
                        equipamento_id = equipamentoId
                    )
                    SupabaseClient.service.atualizarExercicio("eq.$exercicioId", exercicioEditado)
                    runOnUiThread {
                        Toast.makeText(this@TelaEditarExercicioActivity, "Exercício atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@TelaEditarExercicioActivity, "Erro ao atualizar: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun carregarEquipamentos(spinner: Spinner, onFinish: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val equipamentos = SupabaseClient.service.getEquipamentos()
                listaEquipamentos = equipamentos
                val nomes = equipamentos.map { it.nome }
                runOnUiThread {
                    val adapter = ArrayAdapter(this@TelaEditarExercicioActivity, android.R.layout.simple_spinner_item, nomes)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                    onFinish()
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@TelaEditarExercicioActivity, "Erro ao carregar equipamentos", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    private fun carregarExercicio(editNome: EditText, editDescricao: EditText, spinner: Spinner) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lista = SupabaseClient.service.getExercicios("*")
                val exercicio = lista.find { it.id == exercicioId }
                exercicioOriginal = exercicio
                runOnUiThread {
                    if (exercicio != null) {
                        editNome.setText(exercicio.nome)
                        editDescricao.setText(exercicio.descricao ?: "")
                        val pos = listaEquipamentos.indexOfFirst { it.id == exercicio.equipamento_id }
                        if (pos >= 0) spinner.setSelection(pos)
                    } else {
                        Toast.makeText(this@TelaEditarExercicioActivity, "Exercício não encontrado", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@TelaEditarExercicioActivity, "Erro ao carregar exercício", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }
} 