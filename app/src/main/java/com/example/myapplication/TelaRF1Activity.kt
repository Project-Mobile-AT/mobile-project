package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.SupabaseClient // Importa o SupabaseClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TelaRF1Activity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etSenha: EditText
    private lateinit var btnLogin: Button
    private lateinit var recuperarSenha: TextView
    private lateinit var criarConta: TextView

    // Usa a instância do serviço do SupabaseClient
    private val supabaseService = SupabaseClient.service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf1)

        // Inicializa as views
        etEmail = findViewById(R.id.et_email)
        etSenha = findViewById(R.id.et_senha)
        btnLogin = findViewById(R.id.btn_login)
        recuperarSenha = findViewById(R.id.tv_esqueceu_senha)
        criarConta = findViewById(R.id.tv_registre_se)

        // Adiciona sublinhado aos textos
        recuperarSenha.paint.isUnderlineText = true
        criarConta.paint.isUnderlineText = true

        // Listener para o botão de login
        btnLogin.setOnClickListener {
            loginUsuario()
        }

        // Listener para "Esqueceu a senha?"
        recuperarSenha.setOnClickListener {
            val intent = Intent(this, TelaRF1_1Activity::class.java)
            startActivity(intent)
        }

        // Listener para "Registre-se"
        criarConta.setOnClickListener {
            val intent = Intent(this, TelaRF2Activity::class.java)
            startActivity(intent)
        }
    }

    private fun loginUsuario() {
        val email = etEmail.text.toString().trim()
        val senha = etSenha.text.toString().trim()

        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                // Busca um usuário onde o email e a senha correspondam.
                // Lembre-se: Para produção, use autenticação do Supabase ou hashing de senhas.
                val usuarios = supabaseService.getUsuarioByEmailAndPassword(
                    email = "eq.$email",
                    senha = "eq.$senha"
                )

                withContext(Dispatchers.Main) {
                    if (usuarios.isNotEmpty()) {
                        val usuarioLogado = usuarios.first()
                        Toast.makeText(this@TelaRF1Activity, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show()

                        // Salva o ID do usuário no SharedPreferences
                        saveUserId(usuarioLogado.id)
                        Log.d("Login", "ID do usuário logado: ${usuarioLogado.id}")

                        // Redireciona sempre para TelaRF3Activity se o login for bem-sucedido
                        val intent = Intent(this@TelaRF1Activity, TelaRF3Activity::class.java)
                        startActivity(intent)
                        finish() // Fecha a tela de login
                    } else {
                        // Credenciais inválidas
                        Toast.makeText(this@TelaRF1Activity, "Email ou senha inválidos. Tente novamente.", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("LoginError", "Erro ao fazer login: ${e.message}", e)
                    Toast.makeText(this@TelaRF1Activity, "Erro ao conectar. Verifique sua conexão ou tente novamente.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun saveUserId(userId: String) {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("logged_in_user_id", userId)
            apply()
        }
    }

    // Função para recuperar o ID do usuário (exemplo de como usar em outras activities)

    fun getUserId(): String? {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("logged_in_user_id", null)
    }

//    --- FUNÇÃO PARA RECUPERAR O ID DO USUÁRIO ---
//    fun getUserId(): String? {
//        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        return sharedPref.getString("logged_in_user_id", null)
//    }
}