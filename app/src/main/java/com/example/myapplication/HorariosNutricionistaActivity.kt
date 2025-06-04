package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.Aula
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.HorarioAtendimento
import androidx.appcompat.app.AlertDialog
import com.example.myapplication.model.HorarioNutricionista

class HorariosNutricionistaActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HorariosNutricionistaAdapter
    private var horarios: List<HorarioNutricionista> = listOf()
    private lateinit var addHorario: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_horarios_nutricionista)
        
        recyclerView = findViewById(R.id.listaHorariosContainer)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HorariosNutricionistaAdapter(horarios, onEditar = { editarHorario(it) }, onDeletar = { deletarHorario(it) })
        recyclerView.adapter = adapter

        addHorario = findViewById(R.id.btn_add_horario)
        addHorario.setOnClickListener {
            val intent = Intent(this, AddEditHorarioActivity::class.java)
            startActivity(intent)
        }

        carregarHorarios()
    }

    override fun onResume() {
        super.onResume()
        carregarHorarios()
    }

    private fun carregarHorarios() {
        // Buscar horários do Supabase
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val lista = SupabaseClient.service.getHorariosNutricionista()
                withContext(Dispatchers.Main) {
                    horarios = lista
                    adapter.updateList(horarios)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Exibir erro
                }
            }
        }
    }

    private fun editarHorario(horario: HorarioNutricionista) {
        val intent = Intent(this, AddEditHorarioActivity::class.java)
        intent.putExtra("HORARIO_ID", horario.id)
        intent.putExtra("DATA_HORA", horario.data_hora)
        intent.putExtra("DISPONIVEL", horario.disponivel)
        startActivity(intent)
    }

    private fun deletarHorario(horario: HorarioNutricionista) {
        // Exibe um AlertDialog de confirmação
        AlertDialog.Builder(this)
            .setTitle("Excluir horário")
            .setMessage("Tem certeza que deseja excluir este horário?")
            .setPositiveButton("Sim") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        SupabaseClient.service.deletarHorarioNutricionista("eq.${horario.id}")
                        withContext(Dispatchers.Main) {
                            carregarHorarios()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            // Exibir erro
                        }
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
} 