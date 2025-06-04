package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TelaRF17_2Activity : AppCompatActivity() {
    private lateinit var dateInput: TextInputEditText
    private lateinit var startTimeInput: TextInputEditText
    private lateinit var endTimeInput: TextInputEditText
    private lateinit var slotsInput: TextInputEditText
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17_2)
        enableEdgeToEdge()

        // Inicializar views
        dateInput = findViewById(R.id.dateInput)
        startTimeInput = findViewById(R.id.startTimeInput)
        endTimeInput = findViewById(R.id.endTimeInput)
        slotsInput = findViewById(R.id.slotsInput)
        val backButton = findViewById<ImageButton>(R.id.backButton)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        // Configurar DatePicker para o campo de data
        dateInput.setOnClickListener {
            showDatePicker()
        }

        // Configurar TimePicker para o campo de horário inicial
        startTimeInput.setOnClickListener {
            showTimePicker(startTimeInput)
        }

        // Configurar TimePicker para o campo de horário final
        endTimeInput.setOnClickListener {
            showTimePicker(endTimeInput)
        }

        // Botão voltar
        backButton.setOnClickListener {
            finish()
        }

        // Botão salvar
        btnSalvar.setOnClickListener {
            // TODO: Implementar lógica de salvamento
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

    private fun showTimePicker(input: TextInputEditText) {
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hour, minute ->
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                updateTimeInput(input)
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

    private fun updateTimeInput(input: TextInputEditText) {
        val timeFormat = SimpleDateFormat("HH:mm", Locale("pt", "BR"))
        input.setText(timeFormat.format(calendar.time))
    }
}