package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.fragments.BottomNavFragment

class TelaRF3_1Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf3_1)

        val menuButton = findViewById<ImageView>(R.id.id_menu)

        menuButton.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.menu_popup, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_informativos -> {
                        Toast.makeText(this, "Abrindo Informativos", Toast.LENGTH_SHORT).show()
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