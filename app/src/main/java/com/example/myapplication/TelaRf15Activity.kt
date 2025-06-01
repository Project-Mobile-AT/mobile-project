package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.Usuario
import kotlinx.coroutines.launch
import java.util.*

class TelaRf15Activity : AppCompatActivity() {

    private lateinit var etNome: EditText
    private lateinit var etCpf: EditText
    private lateinit var etDataNascimento: EditText
    private lateinit var etPeso: EditText
    private lateinit var etAltura: EditText
    private lateinit var etEmail: EditText
    private lateinit var etSenha: EditText
    private lateinit var btnConfirmar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf15)

        etNome = findViewById(R.id.etNome)
        etCpf = findViewById(R.id.etCpf)
        etDataNascimento = findViewById(R.id.etDataNascimento)
        etPeso = findViewById(R.id.etPeso)
        etAltura = findViewById(R.id.etAltura)
        etEmail = findViewById(R.id.etEmail)
        etSenha = findViewById(R.id.etSenha)
        btnConfirmar = findViewById(R.id.btnConfirmar)

        findViewById<ImageView>(R.id.btnVoltar).setOnClickListener {
            finish()
        }

        btnConfirmar.setOnClickListener {
            val nome = etNome.text.toString()
            val cpf = etCpf.text.toString()
            val dataNascimento = etDataNascimento.text.toString().ifBlank { null }
            val peso = etPeso.text.toString().toDoubleOrNull()
            val altura = etAltura.text.toString().toDoubleOrNull()
            val email = etEmail.text.toString()
            val senha = etSenha.text.toString()

            if (nome.isNotBlank() && cpf.isNotBlank() && email.isNotBlank() && senha.isNotBlank()) {
                val novoUsuario = Usuario(
                    id = UUID.randomUUID().toString(),
                    email = email,
                    senha = senha,
                    is_admin = false,
                    nome = nome,
                    cpf = cpf,
                    data_nascimento = dataNascimento,
                    peso = peso,
                    altura = altura,
                    criado_em = null
                )

                lifecycleScope.launch {
                    try {
                        val usuariosCriados = SupabaseClient.service.criarUsuario(novoUsuario)
                        val usuarioCriado = usuariosCriados.firstOrNull()

                        if (usuarioCriado != null) {
                            Log.d("SUPABASE", "Usuário cadastrado: $usuarioCriado")
                            Toast.makeText(this@TelaRf15Activity, "Aluno cadastrado com sucesso!", Toast.LENGTH_SHORT).show()

                            // 👉 Redireciona para a TelaRF18Activity
                            val intent = Intent(this@TelaRf15Activity, TelaRF18Activity::class.java)
                            startActivity(intent)
                            finish() // Fecha a tela atual para evitar retorno com back
                        } else {
                            Toast.makeText(this@TelaRf15Activity, "Erro ao cadastrar: resposta vazia", Toast.LENGTH_LONG).show()
                        }

                    } catch (e: Exception) {
                        Log.e("SUPABASE", "Erro ao cadastrar aluno: ${e.message}", e)
                        Toast.makeText(this@TelaRf15Activity, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                Toast.makeText(this, "Preencha os campos obrigatórios", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
