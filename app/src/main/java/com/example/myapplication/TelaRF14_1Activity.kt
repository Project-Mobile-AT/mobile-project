package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.Aula
import com.example.myapplication.model.HorarioAtendimento
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TelaRF14_1Activity : AppCompatActivity() {
    private lateinit var editTitulo: TextInputEditText
    private lateinit var editDescricao: TextInputEditText
    private lateinit var editInstrutor: TextInputEditText
    private lateinit var spinnerHorario: Spinner
    private lateinit var btnAtualizar: MaterialButton
    private lateinit var btnDeletar: MaterialButton
    private lateinit var btnBack: ImageView

    private var horarios: List<HorarioAtendimento> = emptyList()
    private var horarioSelecionadoId: String? = null
    private var aulaId: String? = null
    private var aulaAtual: Aula? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf14_1)

        // Inicializar views
        editTitulo = findViewById(R.id.edit_titulo)
        editDescricao = findViewById(R.id.edit_descricao)
        editInstrutor = findViewById(R.id.edit_instrutor)
        spinnerHorario = findViewById(R.id.spinner_horario)
        btnAtualizar = findViewById(R.id.btn_atualizar_aula)
        btnDeletar = findViewById(R.id.btn_deletar_aula)
        btnBack = findViewById(R.id.btn_back)

        aulaId = intent.getStringExtra("aula_id")
        if (aulaId == null) {
            Toast.makeText(this, "Aula não encontrada", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        carregarHorariosEPreencherAula()

        btnBack.setOnClickListener { finish() }
        btnAtualizar.setOnClickListener { atualizarAula() }
        btnDeletar.setOnClickListener { deletarAula() }
    }

    private fun carregarHorariosEPreencherAula() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val horariosResult = SupabaseClient.service.getHorariosAtendimento()
                val aulas = SupabaseClient.service.getAulas()
                withContext(Dispatchers.Main) {
                    horarios = horariosResult
                    val opcoes = horarios.map { it.data_hora + " - " + it.tipo }
                    val adapter = ArrayAdapter(this@TelaRF14_1Activity, android.R.layout.simple_spinner_item, opcoes)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerHorario.adapter = adapter

                    aulaAtual = aulas.find { it.id == aulaId }
                    if (aulaAtual == null) {
                        Toast.makeText(this@TelaRF14_1Activity, "Aula não encontrada", Toast.LENGTH_SHORT).show()
                        finish()
                        return@withContext
                    }

                    // Preencher campos
                    editTitulo.setText(aulaAtual?.titulo)
                    editDescricao.setText(aulaAtual?.descricao)
                    editInstrutor.setText(aulaAtual?.instrutor)
                    // Selecionar o horário correto
                    val idx = horarios.indexOfFirst { it.id == aulaAtual?.horario_id }
                    if (idx >= 0) {
                        spinnerHorario.setSelection(idx)
                        horarioSelecionadoId = horarios[idx].id
                    }

                    spinnerHorario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            horarioSelecionadoId = horarios[position].id
                        }
                        override fun onNothingSelected(parent: AdapterView<*>) {
                            horarioSelecionadoId = null
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TelaRF14_1Activity, "Erro ao carregar dados: ${e.message}", Toast.LENGTH_LONG).show()
                    finish()
                }
            }
        }
    }

    private fun atualizarAula() {
        val titulo = editTitulo.text.toString()
        val descricao = editDescricao.text?.toString() ?: ""
        val instrutor = editInstrutor.text.toString()
        val horarioId = horarioSelecionadoId
        val id = aulaId

        if (titulo.isEmpty() || horarioId.isNullOrEmpty() || instrutor.isEmpty() || id.isNullOrEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }

        val aulaAtualizada = com.example.myapplication.model.AulaPatch(
            horario_id = horarioId,
            titulo = titulo,
            descricao = descricao,
            instrutor = instrutor,
            status = "disponivel"
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                SupabaseClient.service.atualizarAula("eq.$id", aulaAtualizada)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TelaRF14_1Activity, "Aula atualizada com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TelaRF14_1Activity, "Erro ao atualizar aula: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun deletarAula() {
        val id = aulaId ?: return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                SupabaseClient.service.deletarAula("eq.$id")
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TelaRF14_1Activity, "Aula deletada com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TelaRF14_1Activity, "Erro ao deletar aula: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
} 