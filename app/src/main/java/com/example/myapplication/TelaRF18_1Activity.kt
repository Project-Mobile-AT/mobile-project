package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.Usuario
import kotlinx.coroutines.launch

class TelaRF18_1Activity : AppCompatActivity() {
    private lateinit var etNome: EditText
    private lateinit var etCpf: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnDeletar: Button
    private var usuarioId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf18_1)

        etNome = findViewById(R.id.etNome)
        etCpf = findViewById(R.id.etCpf)
        etEmail = findViewById(R.id.etEmail)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnDeletar = findViewById(R.id.btnDeletar)

        usuarioId = intent.getStringExtra("ID")
        etNome.setText(intent.getStringExtra("NOME") ?: "")
        etCpf.setText(intent.getStringExtra("CPF") ?: "")
        etEmail.setText(intent.getStringExtra("EMAIL") ?: "")

        btnSalvar.setOnClickListener {
            val nome = etNome.text.toString()
            val cpf = etCpf.text.toString()
            val email = etEmail.text.toString()
            if (usuarioId != null && nome.isNotBlank() && cpf.isNotBlank() && email.isNotBlank()) {
                val usuarioAtualizado = Usuario(
                    id = usuarioId!!,
                    email = email,
                    senha = null, // Não editável aqui
                    is_admin = false,
                    nome = nome,
                    cpf = cpf,
                    data_nascimento = null,
                    peso = null,
                    altura = null,
                    criado_em = null,
                    img_perfil = null // Adicionando o parâmetro que estava faltando
                )
                lifecycleScope.launch {
                    try {
                        SupabaseClient.service.atualizarUsuario("eq.$usuarioId", usuarioAtualizado)
                        Toast.makeText(this@TelaRF18_1Activity, "Aluno atualizado com sucesso!", Toast.LENGTH_SHORT).show()
                        finish()
                    } catch (e: Exception) {
                        if (e is retrofit2.HttpException) {
                            val errorBody = e.response()?.errorBody()?.string()
                            Log.e("SUPABASE", "Erro ao atualizar: "+e.message+" - "+errorBody, e)
                            Toast.makeText(this@TelaRF18_1Activity, "Erro ao atualizar: $errorBody", Toast.LENGTH_LONG).show()
                        } else {
                            Log.e("SUPABASE", "Erro ao atualizar: ${e.message}", e)
                            Toast.makeText(this@TelaRF18_1Activity, "Erro ao atualizar: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }

        btnDeletar.setOnClickListener {
            if (usuarioId != null) {
                lifecycleScope.launch {
                    try {
                        val response = SupabaseClient.service.deletarUsuario("eq.$usuarioId")
                        if (response.isSuccessful) {
                            Toast.makeText(this@TelaRF18_1Activity, "Aluno deletado com sucesso!", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val errorBody = response.errorBody()?.string()
                            Log.e("SUPABASE", "Erro ao deletar: $errorBody")
                            Toast.makeText(this@TelaRF18_1Activity, "Erro ao deletar: $errorBody", Toast.LENGTH_LONG).show()
                        }
                    } catch (e: Exception) {
                        if (e is retrofit2.HttpException) {
                            val errorBody = e.response()?.errorBody()?.string()
                            Log.e("SUPABASE", "Erro ao deletar: "+e.message+" - "+errorBody, e)
                            Toast.makeText(this@TelaRF18_1Activity, "Erro ao deletar: $errorBody", Toast.LENGTH_LONG).show()
                        } else {
                            Log.e("SUPABASE", "Erro ao deletar: ${e.message}", e)
                            Toast.makeText(this@TelaRF18_1Activity, "Erro ao deletar: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}