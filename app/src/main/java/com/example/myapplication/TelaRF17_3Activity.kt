package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.HorarioAtendimento
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TelaRF17_3Activity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HorariosAlunoAdapter
    private var horarios: List<HorarioAtendimento> = listOf()
    private lateinit var emptyState: TextView
    private var alunoId: String = "aluno_id_placeholder" // TODO: pegar do login
    private var alunoNome: String = "Aluno Nome" // TODO: pegar do login

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17_3)
        enableEdgeToEdge()

        val btn_back = findViewById<ImageButton>(R.id.backButton)
        btn_back.setOnClickListener { finish() }

        recyclerView = findViewById(R.id.recyclerViewHorarios)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HorariosAlunoAdapter(horarios) { horario -> agendarHorario(horario) }
        recyclerView.adapter = adapter

        emptyState = findViewById(R.id.tvEmptyState)

        carregarHorariosDisponiveis()
    }

    private fun carregarHorariosDisponiveis() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lista = SupabaseClient.service.getHorariosAtendimento()
                val disponiveis = lista.filter { it.disponivel }
                withContext(Dispatchers.Main) {
                    horarios = disponiveis
                    adapter.updateList(horarios)
                    emptyState.visibility = if (horarios.isEmpty()) View.VISIBLE else View.GONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    emptyState.visibility = View.VISIBLE
                    emptyState.text = "Erro ao carregar horários."
                }
            }
        }
    }

    private fun agendarHorario(horario: HorarioAtendimento) {
        // Atualiza o registro: disponivel = false, aluno_id, aluno_nome, tipo = "consulta"
        val horarioAtualizado = horario.copy(
            disponivel = false,
            aluno_id = alunoId,
            aluno_nome = alunoNome,
            tipo = "consulta"
        )
        CoroutineScope(Dispatchers.IO).launch {
            try {
                SupabaseClient.service.atualizarHorarioAtendimento("eq.${horario.id}", horarioAtualizado)
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TelaRF17_3Activity, "Horário agendado com sucesso!", Toast.LENGTH_SHORT).show()
                    carregarHorariosDisponiveis()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TelaRF17_3Activity, "Erro ao agendar: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}

