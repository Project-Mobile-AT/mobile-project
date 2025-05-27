package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.fragments.AcessibilidadePopup
import com.example.myapplication.fragments.BottomNavFragment
import com.example.myapplication.fragments.ChatbotPopupFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TelaRF3Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf3)

        val perfil = findViewById<ImageView>(R.id.ic_per)

        val menuButton = findViewById<ImageView>(R.id.imageView)

        val buttonMinhaFicha = findViewById<Button>(R.id.btn_minhaFicha)
        val btn_checkin = findViewById<Button>(R.id.btn_checkin)
        val fabChat = findViewById<FloatingActionButton>(R.id.fab_chat)
        val fabAcessibilidade = findViewById<FloatingActionButton>(R.id.fab_acessibilidade)
        val imageView = findViewById<ImageView>(R.id.imageView)


        perfil.setOnClickListener {
            val intent = Intent(this, TelaRF8Activity::class.java)
            startActivity(intent)
        }

        buttonMinhaFicha.setOnClickListener {
            val intent = Intent(this, TelaRF5Activity::class.java)
            startActivity(intent)
        }

        fabChat.setOnClickListener {
            val chatbotPopup = ChatbotPopupFragment()
            chatbotPopup.show(supportFragmentManager, "ChatbotPopup")
        }

        fabAcessibilidade.setOnClickListener {
            val chatbotPopup =  AcessibilidadePopup()
            chatbotPopup.show(supportFragmentManager, "AcessibilidadePopup")
        }
        imageView.setOnClickListener {
            val intent = Intent(this, TelaRF9Activity::class.java)
            startActivity(intent)
        }



        //Inserir menu hamburguer
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

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_bottom_nav, BottomNavFragment())
            .commit()

    }
}