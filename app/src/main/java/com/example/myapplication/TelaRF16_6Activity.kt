package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.RelacaoAdminAluno
import com.example.myapplication.model.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class TelaRF16_6Activity : AppCompatActivity() {

    private fun getUserId(): String? {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("logged_in_user_id", null)
    }

    private val supabaseService = SupabaseClient.service
    private lateinit var containerAlunos: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf16_6)

        containerAlunos = findViewById(R.id.container_alunos)

        carregarAlunos()
    }

    private fun carregarAlunos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val alunos = supabaseService.getAlunos()

                withContext(Dispatchers.Main) {
                    for (aluno in alunos) {
                        adicionarAlunoNaTela(aluno)
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@TelaRF16_6Activity, "Erro ao buscar alunos: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun adicionarAlunoNaTela(aluno: Usuario) {
        val inflater = LayoutInflater.from(this)
        val view = inflater.inflate(R.layout.item_add_aluno, containerAlunos, false)

        val nomeTextView = view.findViewById<TextView>(R.id.text_nome_aluno)
        val btnAdicionar = view.findViewById<Button>(R.id.btn_adicionar_aluno)
        val avatar = view.findViewById<ImageView>(R.id.image_avatar)

        nomeTextView.text = aluno.nome ?: "Aluno sem nome"

        btnAdicionar.setOnClickListener {
            val adminId = getUserId()

            if (adminId == null) {
                Toast.makeText(this, "Admin não identificado", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val relacao = RelacaoAdminAluno(
                id = UUID.randomUUID().toString(),
                admin_id = adminId,
                aluno_id = aluno.id,
                tipo = "personal"
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val resultado = supabaseService.createRelacaoAdminAluno(relacao)

                    withContext(Dispatchers.Main) {
                        if (resultado.isNotEmpty()) {
                            Toast.makeText(this@TelaRF16_6Activity, "Aluno vinculado com sucesso!", Toast.LENGTH_LONG).show()
                            val intent = Intent(this@TelaRF16_6Activity, TelaRF16_5Activity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@TelaRF16_6Activity, "Falha ao vincular aluno.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@TelaRF16_6Activity, "Erro: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }



        containerAlunos.addView(view)
    }
}
