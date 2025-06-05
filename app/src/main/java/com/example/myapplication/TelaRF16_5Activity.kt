package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Context
import android.widget.LinearLayout
import android.widget.Toast
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.SupabaseClient.service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient
import kotlinx.coroutines.withContext

class TelaRF16_5Activity : AppCompatActivity() {
//ID do admin logado, que foi salvo no SharedPreferences
    private fun getUserId(): String? {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("logged_in_user_id", null)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf16_5)

        val layoutContainer = findViewById<LinearLayout>(R.id.alunoListContainer)
        val userId = getUserId()

        if (userId != null) {
            //corroutine no Dispatchers.IO (thread para operações pesadas, como chamadas de rede).
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    //Recebe o resultado da requisição ao Supabase,
                    val alunosRelacionados = SupabaseClient.service.getAlunosByAdminId("eq.$userId")
                    val listaDeAlunos = alunosRelacionados.mapNotNull { it.aluno }

                    withContext(Dispatchers.Main) {
                    // thread principal (UI) para atualizar a tela com os dados recebidos.
                        layoutContainer.removeAllViews() //Remove os cards antigos (caso volte para essa tela ou atualize).
                        //Percorre a lista de alunos e infla o layout de card
                        for (aluno in listaDeAlunos) {
                            val cardView = layoutInflater.inflate(R.layout.item_aluno_card_personal, layoutContainer, false)

                            val nomeTextView = cardView.findViewById<TextView>(R.id.nome_aluno)

                            val btnFicha = cardView.findViewById<Button>(R.id.btn_visualizar_ficha)

                            nomeTextView.text = aluno.nome


                            btnFicha.setOnClickListener {
                                val intent = Intent(this@TelaRF16_5Activity, TelaRF12_1Activity::class.java)
                                intent.putExtra("alunoId", aluno.id)
                                startActivity(intent)
                            }

                            layoutContainer.addView(cardView)
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@TelaRF16_5Activity, "Erro ao buscar alunos", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        findViewById<Button>(R.id.btnAdicionarAluno).setOnClickListener {
            startActivity(Intent(this, TelaRF16_6Activity::class.java))
        }
    }
}

