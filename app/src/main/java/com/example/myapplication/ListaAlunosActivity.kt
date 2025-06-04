package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.Usuario
import kotlinx.coroutines.launch

class ListaAlunosActivity : AppCompatActivity() {
    private lateinit var adapter: ListaAlunosAdapter
    private var alunos: List<Usuario> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_alunos)
        enableEdgeToEdge()

        val backButton = findViewById<ImageButton>(R.id.backButton)
        val searchInput = findViewById<EditText>(R.id.searchInput)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val progressBar = ProgressBar(this)
        addContentView(progressBar, RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT))
        progressBar.visibility = View.VISIBLE

        adapter = ListaAlunosAdapter(alunos) { aluno ->
            val intent = Intent(this, FichaNutricionalActivity::class.java)
            intent.putExtra("aluno_id", aluno.id)
            intent.putExtra("aluno_nome", aluno.nome)
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE
                val resultado = SupabaseClient.service.getAlunos()
                alunos = resultado
                adapter.updateList(alunos)
                progressBar.visibility = View.GONE
            } catch (e: Exception) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@ListaAlunosActivity, "Erro ao buscar alunos: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

        searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val filtered = alunos.filter { it.nome.contains(s.toString(), ignoreCase = true) }
                adapter.updateList(filtered)
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        backButton.setOnClickListener { finish() }
    }
} 