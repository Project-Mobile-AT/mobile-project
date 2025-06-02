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
import java.util.UUID

class TelaRF14_2Activity : AppCompatActivity() {
    private lateinit var editTitulo: TextInputEditText
    private lateinit var editDescricao: TextInputEditText
    private lateinit var editInstrutor: TextInputEditText
    private lateinit var spinnerHorario: Spinner
    private lateinit var btnSalvar: MaterialButton
    private lateinit var btnBack: ImageView

    private var horarios: List<HorarioAtendimento> = emptyList()
    private var horarioSelecionadoId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf14_2)

        // Inicializar views
        editTitulo = findViewById(R.id.edit_titulo)
        editDescricao = findViewById(R.id.edit_descricao)
        editInstrutor = findViewById(R.id.edit_instrutor)
        spinnerHorario = findViewById(R.id.spinner_horario)
        btnSalvar = findViewById(R.id.btn_salvar)
        btnBack = findViewById(R.id.btn_back)

        // Buscar horários do Supabase
        carregarHorarios()

        // Configurar botão voltar
        btnBack.setOnClickListener {
            finish()
        }

        // Configurar botão salvar
        btnSalvar.setOnClickListener {
            salvarAula()
        }
    }

    private fun carregarHorarios() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val horariosResult = SupabaseClient.service.getHorariosAtendimento()
                withContext(Dispatchers.Main) {
                    horarios = horariosResult
                    val opcoes = horarios.map { it.data_hora + " - " + it.tipo }
                    val adapter = ArrayAdapter(this@TelaRF14_2Activity, android.R.layout.simple_spinner_item, opcoes)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinnerHorario.adapter = adapter
                    spinnerHorario.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                            horarioSelecionadoId = horarios[position].id
                        }
                        override fun onNothingSelected(parent: AdapterView<*>) {
                            horarioSelecionadoId = null
                        }
                    }
                    Toast.makeText(
                        this@TelaRF14_2Activity,
                        "Horários recebidos: ${horarios.size}\n" + horarios.joinToString("\n") { it.data_hora + " - " + it.tipo + " (id: " + it.id + ")" },
                        Toast.LENGTH_LONG
                    ).show()
                    if (horarios.isEmpty()) {
                        Toast.makeText(this@TelaRF14_2Activity, "Nenhum horário cadastrado!", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TelaRF14_2Activity, "Erro ao carregar horários: ${e.message}\n${e.stackTraceToString()}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun salvarAula() {
        val titulo = editTitulo.text.toString()
        val descricao = editDescricao.text?.toString() ?: ""
        val instrutor = editInstrutor.text.toString()
        val horarioId = horarioSelecionadoId

        if (titulo.isEmpty() || horarioId.isNullOrEmpty() || instrutor.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
            return
        }

        val novaAula = com.example.myapplication.model.AulaPost(
            horario_id = horarioId,
            titulo = titulo,
            descricao = descricao,
            instrutor = instrutor,
            status = "disponivel"
        )

        Toast.makeText(this, "Enviando: $novaAula", Toast.LENGTH_LONG).show()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                SupabaseClient.service.criarAula(novaAula)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TelaRF14_2Activity, "Aula criada com sucesso!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TelaRF14_2Activity, "Erro ao criar aula: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
} 