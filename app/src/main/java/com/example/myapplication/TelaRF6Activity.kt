package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.fragments.BottomNavFragment

class TelaRF6Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_tela_rf6)

        val menuButton = findViewById<ImageView>(R.id.ivMenu)

        val perfil = findViewById<ImageView>(R.id.ivProfile)

        perfil.setOnClickListener {
            val intent = Intent(this, TelaRF8Activity::class.java)
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
                    R.id.menu_sair -> {
                        Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show()
                        finish()
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