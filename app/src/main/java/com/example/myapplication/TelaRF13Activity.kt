package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.Equipamento
import kotlinx.coroutines.launch

class TelaRF13Activity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: EquipamentoAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var textEmpty: TextView
    private lateinit var btnAdicionarEquipamento: Button

    private val cadastroLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            carregarEquipamentos()
        }
    }

    private val edicaoLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            carregarEquipamentos()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf13)

        inicializarViews()
        setupRecyclerView()
        carregarEquipamentos()
    }

    private fun inicializarViews() {
        recyclerView = findViewById(R.id.recyclerViewEquipamentos)
        progressBar = findViewById(R.id.progressBar)
        textEmpty = findViewById(R.id.textEmpty)
        btnAdicionarEquipamento = findViewById(R.id.btnAdicionarEquipamento)

        btnAdicionarEquipamento.setOnClickListener {
            val intent = Intent(this, TelaRF13_2Activity::class.java)
            cadastroLauncher.launch(intent)
        }
    }

    private fun setupRecyclerView() {
        adapter = EquipamentoAdapter(
            equipamentos = emptyList(),
            onEditarClick = { equipamento ->
                val intent = Intent(this, TelaRF13_1Activity::class.java).apply {
                    putExtra("equipamento_id", equipamento.id)
                    putExtra("equipamento_nome", equipamento.nome)
                    putExtra("equipamento_descricao", equipamento.descricao)
                }
                edicaoLauncher.launch(intent)
            },
            onExcluirClick = { equipamento ->
                confirmarExclusao(equipamento)
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    private fun confirmarExclusao(equipamento: Equipamento) {
        AlertDialog.Builder(this)
            .setTitle("Confirmar exclusão")
            .setMessage("Deseja realmente excluir o equipamento ${equipamento.nome}?")
            .setPositiveButton("Sim") { _, _ ->
                excluirEquipamento(equipamento)
            }
            .setNegativeButton("Não", null)
            .show()
    }

    private fun excluirEquipamento(equipamento: Equipamento) {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.service.deletarEquipamento("eq.${equipamento.id}")
                if (response.isSuccessful) {
                    Toast.makeText(this@TelaRF13Activity, 
                        "Equipamento excluído com sucesso!", 
                        Toast.LENGTH_SHORT).show()
                    carregarEquipamentos()
                } else {
                    showLoading(false)
                    Toast.makeText(this@TelaRF13Activity, 
                        "Erro ao excluir equipamento", 
                        Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                showLoading(false)
                Toast.makeText(this@TelaRF13Activity, 
                    "Erro ao excluir: ${e.message}", 
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun carregarEquipamentos() {
        showLoading(true)
        lifecycleScope.launch {
            try {
                val equipamentos = SupabaseClient.service.getEquipamentos()
                showLoading(false)
                if (equipamentos.isNotEmpty()) {
                    adapter.atualizarLista(equipamentos)
                    showEmptyState(false)
                } else {
                    showEmptyState(true)
                }
            } catch (e: Exception) {
                showLoading(false)
                showEmptyState(true)
                Toast.makeText(this@TelaRF13Activity, 
                    "Erro ao carregar equipamentos: ${e.message}", 
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showEmptyState(show: Boolean) {
        textEmpty.visibility = if (show) View.VISIBLE else View.GONE
        recyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }
}

class EquipamentoAdapter(
    private var equipamentos: List<Equipamento>,
    private val onEditarClick: (Equipamento) -> Unit,
    private val onExcluirClick: (Equipamento) -> Unit
) : RecyclerView.Adapter<EquipamentoAdapter.EquipamentoViewHolder>() {

    class EquipamentoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nomeEquipamento: TextView = view.findViewById(R.id.tvNome)
        val descricaoEquipamento: TextView = view.findViewById(R.id.tvDescricao)
        val btnEditar: ImageButton = view.findViewById(R.id.btnEditar)
        val btnExcluir: ImageButton = view.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EquipamentoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_equipamento, parent, false)
        return EquipamentoViewHolder(view)
    }

    override fun onBindViewHolder(holder: EquipamentoViewHolder, position: Int) {
        val equipamento = equipamentos[position]
        holder.nomeEquipamento.text = equipamento.nome
        holder.descricaoEquipamento.text = equipamento.descricao ?: "Sem descrição"
        
        holder.btnEditar.setOnClickListener {
            onEditarClick(equipamento)
        }
        
        holder.btnExcluir.setOnClickListener {
            onExcluirClick(equipamento)
        }
    }

    override fun getItemCount() = equipamentos.size

    fun atualizarLista(novaLista: List<Equipamento>) {
        equipamentos = novaLista
        notifyDataSetChanged()
    }
}