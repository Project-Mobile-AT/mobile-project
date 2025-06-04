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

class TelaRF2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf2)

        val inputNome = findViewById<EditText>(R.id.et_nome)
        val inputEmail = findViewById<EditText>(R.id.et_email)
        val inputCpf = findViewById<EditText>(R.id.et_cpf)
        val inputSenha = findViewById<EditText>(R.id.et_senha)

        val btnEntrar = findViewById<Button>(R.id.button)
        val linkLogin = findViewById<TextView>(R.id.link_login)

        // Quando clicar no botão "Entrar" → enviar dados para o Supabase
        btnEntrar.setOnClickListener {
            val nome = inputNome.text.toString()
            val email = inputEmail.text.toString()
            val cpf = inputCpf.text.toString()
            val senha = inputSenha.text.toString()

            //garante que os campos não estão vazios
            if (nome.isNotBlank() && email.isNotBlank() && cpf.isNotBlank() && senha.isNotBlank()) {
                //cria o objeto Usuario
                val novoUsuario = Usuario(
                    id = UUID.randomUUID().toString(),
                    email = email,
                    senha = senha,
                    is_admin = false,
                    nome = nome,
                    cpf = cpf,
                    data_nascimento = null,
                    peso = null,
                    altura = null,
                    criado_em = null,
                    img_perfil = null
                )
                //envia o POST com Retrofit - chamando a função criarUsuario()
                lifecycleScope.launch {
                    try {
                        val usuariosCriados = SupabaseClient.service.criarUsuario(novoUsuario)
                        val usuarioCriado = usuariosCriados.firstOrNull()

                        if (usuarioCriado != null) {
                            Log.d("SUPABASE", "Usuário criado: $usuarioCriado")
                            Toast.makeText(this@TelaRF2Activity, "Bem-vindo, ${usuarioCriado.nome}!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@TelaRF2Activity, TelaRF2_1Activity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@TelaRF2Activity, "Erro: usuário não retornado", Toast.LENGTH_LONG).show()
                        }

                    } catch (e: Exception) {
                        Log.e("SUPABASE", "Erro ao criar usuário: ${e.message}")
                        Toast.makeText(this@TelaRF2Activity, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }

            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }

        linkLogin.setOnClickListener {
            val intent = Intent(this, TelaRF1Activity::class.java)
            startActivity(intent)
        }
    }
}
