package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.HorarioNutricionista
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddEditHorarioActivity : AppCompatActivity() {
    private var horarioId: String? = null
    private lateinit var edtDataHora: EditText
    private lateinit var switchDisponivel: Switch
    private lateinit var btnSalvar: Button
    private lateinit var btnCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_horario)

        edtDataHora = findViewById(R.id.edtDataHora)
        switchDisponivel = findViewById(R.id.switchDisponivel)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnCancelar = findViewById(R.id.btnCancelar)

        // Se for edição, preencher campos
        horarioId = intent.getStringExtra("HORARIO_ID")
        if (horarioId != null) {
            edtDataHora.setText(intent.getStringExtra("DATA_HORA") ?: "")
            switchDisponivel.isChecked = intent.getBooleanExtra("DISPONIVEL", true)
        } else {
            switchDisponivel.isChecked = true
        }

        btnSalvar.setOnClickListener {
            val dataHora = edtDataHora.text.toString().trim()
            val disponivel = switchDisponivel.isChecked
            if (dataHora.isEmpty()) {
                Toast.makeText(this, "Preencha a data e hora.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val horario = HorarioNutricionista(
                id = horarioId ?: UUID.randomUUID().toString(),
                admin_id = "admin_id_placeholder", // Substituir pelo ID real do admin logado
                data_hora = dataHora,
                disponivel = disponivel,
                aluno_id = null,
                aluno_nome = null,
                criado_em = null
            )
            if (horarioId == null) {
                criarHorario(horario)
            } else {
                atualizarHorario(horario)
            }
        }

        btnCancelar.setOnClickListener {
            finish()
        }
    }

    private fun criarHorario(horario: HorarioNutricionista) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                SupabaseClient.service.criarHorarioNutricionista(horario)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddEditHorarioActivity, "Horário criado!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddEditHorarioActivity, "Erro ao criar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun atualizarHorario(horario: HorarioNutricionista) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                SupabaseClient.service.atualizarHorarioNutricionista("eq.${horario.id}", horario)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddEditHorarioActivity, "Horário atualizado!", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@AddEditHorarioActivity, "Erro ao atualizar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
} 