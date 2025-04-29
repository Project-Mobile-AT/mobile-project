package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class TelaRF8_1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf8_1)

        val avaliacaoFisica = findViewById<ImageView>(R.id.avaliacao_fisica)

        avaliacaoFisica.setOnClickListener {
            val intent = Intent(this, TelaRF8Activity::class.java)
            startActivity(intent)
        }

        //Inserir menu hamburguer
        val menuButton = findViewById<ImageButton>(R.id.menuButton)

        menuButton.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.menu_popup, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_dados -> {
                        Toast.makeText(this, "Carregando Dados Pessoais", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, TelaRF3_1Activity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.menu_informativos -> {
                        Toast.makeText(this, "Abrindo Informativos", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, TelaRF8_4Activity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.meus_agendamentos -> {
                        Toast.makeText(this, "Abrindo Agendamentos...", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, TelaRF8_5Activity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.reservar_consultas -> {
                        Toast.makeText(this, "Ação de Reserva...", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, TelaRF7Activity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.menu_sair -> {
                        Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, TelaRF1Activity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            popup.show()
        }
    }

    }
