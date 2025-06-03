package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.Exercicio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TelaRF12Activity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var exercicioAdapter: ExercicioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf12)
        enableEdgeToEdge()

        recyclerView = findViewById(R.id.recyclerViewExercicios)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        val btnVoltar = findViewById<ImageButton>(R.id.btnVoltar)
        val btnAdicionarExercicio = findViewById<Button>(R.id.btnAdicionarExercicio)

        recyclerView.layoutManager = LinearLayoutManager(this)
        exercicioAdapter = ExercicioAdapter(mutableListOf(),
            onEditClick = { exercicio ->
                if (exercicio.id.isNullOrEmpty()) {
                    Toast.makeText(this, "ID do exercício inválido", Toast.LENGTH_SHORT).show()
                    Log.e("EDIT_EXERCICIO", "ID nulo ou vazio ao tentar editar")
                    return@ExercicioAdapter
                }
                val intent = Intent(this, TelaEditarExercicioActivity::class.java)
                intent.putExtra("exercicio_id", exercicio.id)
                Toast.makeText(this, "Abrindo edição para ID: ${exercicio.id}", Toast.LENGTH_SHORT).show()
                startActivity(intent)
            },
            onDeleteClick = { exercicio ->
                AlertDialog.Builder(this)
                    .setTitle("Excluir exercício")
                    .setMessage("Tem certeza que deseja excluir este exercício?")
                    .setPositiveButton("Sim") { _, _ ->
                        deletarExercicio(exercicio.id)
                    }
                    .setNegativeButton("Não", null)
                    .show()
            }
        )
        recyclerView.adapter = exercicioAdapter

        btnVoltar.setOnClickListener {
            finish()
        }

        btnAdicionarExercicio.setOnClickListener {
            val intent = Intent(this, TelaRF12_1Activity::class.java)
            startActivity(intent)
        }

        carregarExercicios()
    }

    private fun carregarExercicios() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lista = SupabaseClient.service.getExercicios()
                runOnUiThread {
                    atualizarEstadoLista(lista)
                }
            } catch (e: Exception) {
                runOnUiThread {
                    atualizarEstadoLista(emptyList())
                    Toast.makeText(this@TelaRF12Activity, "Erro ao buscar exercícios: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun atualizarEstadoLista(exercicios: List<Exercicio>) {
        if (exercicios.isEmpty()) {
            recyclerView.visibility = View.GONE
            emptyStateLayout.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            emptyStateLayout.visibility = View.GONE
            exercicioAdapter.atualizarLista(exercicios)
        }
    }

    private fun deletarExercicio(id: String?) {
        if (id.isNullOrEmpty()) return
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = SupabaseClient.service.deletarExercicio("eq.$id")
                runOnUiThread {
                    if (response.isSuccessful) {
                        Toast.makeText(this@TelaRF12Activity, "Exercício excluído!", Toast.LENGTH_SHORT).show()
                        carregarExercicios()
                    } else {
                        Toast.makeText(this@TelaRF12Activity, "Erro ao excluir: ${response.code()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(this@TelaRF12Activity, "Erro ao excluir: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        carregarExercicios()
    }
}

// Adapter para a lista de exercícios
class ExercicioAdapter(
    private var exercicios: MutableList<Exercicio>,
    private val onEditClick: (Exercicio) -> Unit,
    private val onDeleteClick: (Exercicio) -> Unit
) : RecyclerView.Adapter<ExercicioAdapter.ExercicioViewHolder>() {

    inner class ExercicioViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvNomeExercicio: TextView = view.findViewById(R.id.tvNomeExercicio)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExercicioViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercicio, parent, false)
        return ExercicioViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExercicioViewHolder, position: Int) {
        val exercicio = exercicios[position]
        holder.tvNomeExercicio.text = exercicio.nome
        
        holder.btnEdit.setOnClickListener {
            onEditClick(exercicio)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(exercicio)
        }
    }

    override fun getItemCount() = exercicios.size

    fun atualizarLista(novaLista: List<Exercicio>) {
        exercicios.clear()
        exercicios.addAll(novaLista)
        notifyDataSetChanged()
    }
}