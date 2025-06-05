
package com.example.myapplication // Ajuste o pacote se necessário

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
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
    // Usar o adapter modificado
    private lateinit var adapter: HorariosAlunoAdapter
    private var horarios: List<HorarioAtendimento> = listOf()
    private lateinit var emptyState: TextView
    // Remover alunoId e alunoNome se não forem mais usados nesta tela
    // private var alunoId: String = "aluno_id_placeholder" // TODO: pegar do login
    // private var alunoNome: String = "Aluno Nome" // TODO: pegar do login

    companion object {
        const val EXTRA_HORARIO_ID = "HORARIO_ID" // Chave para passar o ID
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Certifique-se que o layout XML correto está sendo usado
        setContentView(R.layout.activity_tela_rf17_3)
        enableEdgeToEdge()

        val btn_back = findViewById<ImageButton>(R.id.backButton)
        btn_back.setOnClickListener { finish() }

        recyclerView = findViewById(R.id.recyclerViewHorarios)
        recyclerView.layoutManager = LinearLayoutManager(this)
        // Instanciar o adapter modificado, passando a função de navegação
        adapter = HorariosAlunoAdapter(horarios) { horario -> navigateToEditScreen(horario) }
        recyclerView.adapter = adapter

        emptyState = findViewById(R.id.tvEmptyState)

        // Carregar todos os horários
        carregarTodosHorarios()
    }

    // Renomeado e modificado para carregar todos os horários
    private fun carregarTodosHorarios() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Chama o método que busca todos os horários
                val lista = SupabaseClient.service.getHorariosAtendimento()
                // Não filtra mais por 'disponivel'
                withContext(Dispatchers.Main) {
                    horarios = lista
                    adapter.updateList(horarios)
                    emptyState.visibility = if (horarios.isEmpty()) View.VISIBLE else View.GONE
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    emptyState.visibility = View.VISIBLE
                    emptyState.text = "Erro ao carregar horários: ${e.message}"
                    e.printStackTrace() // Ajuda a depurar
                }
            }
        }
    }

    // Função para navegar para a tela de edição
    private fun navigateToEditScreen(horario: HorarioAtendimento) {
        val intent = Intent(this, TelaRF17_4Activity::class.java).apply {
            putExtra(EXTRA_HORARIO_ID, horario.id) // Passa o ID do horário
        }
        startActivity(intent)
    }

    // Remover a função agendarHorario, pois não é mais usada aqui
    /*
    private fun agendarHorario(horario: HorarioAtendimento) {
        // ... lógica antiga de agendamento ...
    }
    */

    // Recarregar a lista quando a tela voltar a ser exibida (após edição, por exemplo)
    override fun onResume() {
        super.onResume()
        carregarTodosHorarios()
    }
}

