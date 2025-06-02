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

class TelaRF14Activity : AppCompatActivity() {
    private lateinit var listaAulasContainer: LinearLayout
    private lateinit var addAula: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf14)
        
        listaAulasContainer = findViewById(R.id.listaAulasContainer)
        addAula = findViewById(R.id.btn_add_class)

        addAula.setOnClickListener {
            val intent = Intent(this, TelaRF14_2Activity::class.java)
            startActivity(intent)
        }

        carregarAulas()
    }

    override fun onResume() {
        super.onResume()
        carregarAulas()
    }

    private fun carregarAulas() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val aulas = SupabaseClient.service.getAulas()
                withContext(Dispatchers.Main) {
                    exibirAulas(aulas)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    // Mostrar mensagem de erro
                }
            }
        }
    }

    private fun exibirAulas(aulas: List<Aula>) {
        listaAulasContainer.removeAllViews()

        if (aulas.isEmpty()) {
            val emptyView = TextView(this).apply {
                text = "Nenhuma aula cadastrada"
                textSize = 16f
                setPadding(16, 16, 16, 16)
            }
            listaAulasContainer.addView(emptyView)
            return
        }

        aulas.forEach { aula ->
            val aulaView = criarAulaView(aula)
            listaAulasContainer.addView(aulaView)
        }
    }

    private fun criarAulaView(aula: Aula): View {
        return layoutInflater.inflate(R.layout.item_aula, listaAulasContainer, false).apply {
            findViewById<TextView>(R.id.txt_titulo).text = aula.titulo
            findViewById<TextView>(R.id.txt_descricao).text = aula.descricao ?: "Sem descrição"
            findViewById<TextView>(R.id.txt_instrutor).text = "Instrutor: ${aula.instrutor ?: "-"}"
            // TODO: Preencher txt_horario se possível
            val btnEditar = findViewById<ImageButton>(R.id.btn_editar)
            btnEditar.setOnClickListener {
                val intent = Intent(this@TelaRF14Activity, TelaRF14_1Activity::class.java)
                intent.putExtra("aula_id", aula.id)
                startActivity(intent)
            }
        }
    }
} 