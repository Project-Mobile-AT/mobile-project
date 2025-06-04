package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TelaRF17_5Activity : AppCompatActivity() {
    private lateinit var dateInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText
    private lateinit var reasonInput: TextInputEditText
    private lateinit var notesInput: TextInputEditText
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17_5)
        enableEdgeToEdge()

        // Inicializar views
        dateInput = findViewById(R.id.dateInput)
        timeInput = findViewById(R.id.timeInput)
        reasonInput = findViewById(R.id.reasonInput)
        notesInput = findViewById(R.id.notesInput)
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val btnConfirmar = findViewById<Button>(R.id.btnConfirmar)

        // Configurar DatePicker para o campo de data
        dateInput.setOnClickListener {
            showDatePicker()
        }

        // Configurar TimePicker para o campo de horário
        timeInput.setOnClickListener {
            showTimePicker()
        }

        // Botão voltar
        backButton.setOnClickListener {
            finish()
        }

        // Botão confirmar
        btnConfirmar.setOnClickListener {
            // TODO: Implementar lógica de confirmação
            val intent = Intent(this, TelaRF17_6Activity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun showDatePicker() {
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                updateDateInput()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                updateTimeInput()
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun updateDateInput() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))
        dateInput.setText(dateFormat.format(calendar.time))
    }

    private fun updateTimeInput() {
        val timeFormat = SimpleDateFormat("HH:mm", Locale("pt", "BR"))
        timeInput.setText(timeFormat.format(calendar.time))
    }
}
