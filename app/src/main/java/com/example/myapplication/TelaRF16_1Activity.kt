package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.data.SupabaseService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TelaRF16_1Activity : AppCompatActivity() {

    private fun getUserId(): String? {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("logged_in_user_id", null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf16_1)

        val nomePersonal = findViewById<TextView>(R.id.tv_trainer_name)

        val userId = getUserId()

        if (userId != null) {
            val supabaseService = SupabaseClient.service

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val usuarios = supabaseService.getUsuarioById("eq.$userId")
                    val usuario = usuarios.firstOrNull()

                    withContext(Dispatchers.Main) {
                        if (usuario != null) {
                            nomePersonal.text = "Olá, ${usuario.nome}"
                        } else {
                            nomePersonal.text = "Olá, Personal"
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        nomePersonal.text = "Erro ao carregar nome"
                    }
                }
            }
        } else {
            nomePersonal.text = "Usuário não logado"
        }

        val listarAluno = findViewById<Button>(R.id.btn_students)

        listarAluno.setOnClickListener {
            val intent = Intent(this, TelaRF16_5Activity::class.java)
            startActivity(intent)
        }


        val visualizarHorarios = findViewById<Button>(R.id.btn_schedule)

        visualizarHorarios.setOnClickListener {
            val intent = Intent(this, TelaRF16_2Activity::class.java)
            startActivity(intent)
        }

        val addAluno = findViewById<Button>(R.id.btn_add_student)

        addAluno.setOnClickListener {
            val intent = Intent(this, TelaRF16_6Activity::class.java)
            startActivity(intent)
        }
    }
}