package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton

class TelaRF14Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf14)
        val addAula = findViewById<MaterialButton>(R.id.btn_add_class)
        val editarAula = findViewById<ImageView>(R.id.edit_class)

        addAula.setOnClickListener {
            val intent = Intent(this, TelaRF14_2Activity::class.java)
            startActivity(intent)
        }
        editarAula.setOnClickListener {
            val intent = Intent(this, TelaRF14_1Activity::class.java)
            startActivity(intent)
        }

    }
}