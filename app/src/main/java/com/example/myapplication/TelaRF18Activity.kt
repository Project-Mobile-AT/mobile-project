package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient
import kotlinx.coroutines.launch

class TelaRF18Activity : AppCompatActivity() {
    private lateinit var listaAlunosContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf18)

        val editarAlunos = findViewById<ImageView>(R.id.editAlunos)

        editarAlunos.setOnClickListener {
            val intent = Intent(this, TelaRF18_1Activity::class.java)
            startActivity(intent)
        }

        val nomeAluno = findViewById<LinearLayout>(R.id.aluno)

        nomeAluno.setOnClickListener {
            val intent = Intent(this, TelaRF18_2Activity::class.java)
            startActivity(intent)
        }

        listaAlunosContainer = findViewById(R.id.listaAlunosContainer)

       // buscarAlunos()
    }

    override fun onStart() {
        super.onStart()
        buscarAlunos()
    }

    private fun buscarAlunos() {
        listaAlunosContainer.removeAllViews()
        lifecycleScope.launch {
            try {
                val alunos = SupabaseClient.service.getAlunos()
                if (alunos.isNotEmpty()) {
                    exibirAlunos(alunos)
                } else {
                    mostrarMensagem("Nenhum aluno encontrado.")
                }
            } catch (e: Exception) {
                Log.e("SUPABASE", "Erro ao buscar alunos: ${e.message}", e)
                mostrarMensagem("Erro ao carregar alunos.")
            }
        }
    }

    private fun exibirAlunos(alunos: List<com.example.myapplication.model.Usuario>) {
        val inflater = LayoutInflater.from(this)
        for (aluno in alunos) {
            val itemView = inflater.inflate(R.layout.item_aluno_card, listaAlunosContainer, false)
            val nomeTextView = itemView.findViewById<TextView>(R.id.nome_aluno)
            nomeTextView.text = aluno.nome
            val btnEditar = itemView.findViewById<ImageView>(R.id.editAlunos)
            btnEditar.setOnClickListener {
                val intent = Intent(this, TelaRF18_1Activity::class.java)
                intent.putExtra("ID", aluno.id)
                intent.putExtra("NOME", aluno.nome)
                intent.putExtra("CPF", aluno.cpf)
                intent.putExtra("EMAIL", aluno.email)
                intent.putExtra("DATA_NASCIMENTO", aluno.data_nascimento)
                intent.putExtra("PESO", aluno.peso)
                intent.putExtra("ALTURA", aluno.altura)
                intent.putExtra("SENHA", aluno.senha)
                startActivity(intent)
            }
            listaAlunosContainer.addView(itemView)
        }
    }

    private fun mostrarMensagem(msg: String) {
        val tv = TextView(this)
        tv.text = msg
        tv.textSize = 16f
        listaAlunosContainer.addView(tv)
    }
}