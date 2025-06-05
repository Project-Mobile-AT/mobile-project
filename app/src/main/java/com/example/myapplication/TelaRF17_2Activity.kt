
package com.example.myapplication

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient // Importar o SupabaseClient
import com.example.myapplication.data.SupabaseService
import com.example.myapplication.model.HorarioAtendimento
import com.example.myapplication.model.Usuario
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class TelaRF17_2Activity : AppCompatActivity() {

    // Views do XML novo (para criar HorarioAtendimento)
    private lateinit var adminInput: AutoCompleteTextView
    private lateinit var tipoInput: AutoCompleteTextView
    private lateinit var dateInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText // Para a hora principal (data_hora)
    private lateinit var startTimeInput: TextInputEditText // Opcional
    private lateinit var endTimeInput: TextInputEditText // Opcional
    private lateinit var dayOfWeekInput: TextInputEditText // Opcional
    private lateinit var btnSalvar: Button
    private lateinit var backButton: ImageButton

    // Adapters e listas de dados
    private lateinit var adminAdapter: ArrayAdapter<String>
    private var adminsList: List<Usuario> = emptyList()

    // Variáveis para guardar seleções
    private var selectedAdminId: String? = null
    private var selectedTipo: String? = null
    private val calendar = Calendar.getInstance() // Para Date/Time Pickers
    private val selectedDateTime = Calendar.getInstance() // Guarda data e hora combinadas
    private val selectedStartTime = Calendar.getInstance() // Guarda hora de início opcional
    private val selectedEndTime = Calendar.getInstance() // Guarda hora de fim opcional

    // Serviço da API
    private val supabaseService: SupabaseService = SupabaseClient.service // Usando o SupabaseClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17_2)
        enableEdgeToEdge()

        // Inicializar views
        adminInput = findViewById(R.id.adminInput)
        tipoInput = findViewById(R.id.tipoInput)
        dateInput = findViewById(R.id.dateInput)
        timeInput = findViewById(R.id.timeInput)
        startTimeInput = findViewById(R.id.startTimeInput)
        endTimeInput = findViewById(R.id.endTimeInput)
        dayOfWeekInput = findViewById(R.id.dayOfWeekInput)
        btnSalvar = findViewById(R.id.btnSalvar)
        backButton = findViewById(R.id.backButton)

        // Configurar dropdowns estáticos e dinâmicos
        setupTipoDropdown()
        setupAdminDropdown()

        // Configurar Date/Time Pickers
        setupPickers()

        // Botão voltar
        backButton.setOnClickListener {
            finish()
        }

        // Botão salvar
        btnSalvar.setOnClickListener {
            salvarHorarioAtendimento()
        }
    }

    private fun setupTipoDropdown() {
        val tipos = listOf("personal", "nutricionista") // Tipos permitidos
        val tipoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipos)
        tipoInput.setAdapter(tipoAdapter)
        tipoInput.setOnItemClickListener { _, _, position, _ ->
            selectedTipo = tipoAdapter.getItem(position)
        }
    }

    private fun setupAdminDropdown() {
        adminAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        adminInput.setAdapter(adminAdapter)

        adminInput.setOnItemClickListener { _, _, position, _ ->
            selectedAdminId = adminsList.getOrNull(position)?.id
        }

        // Buscar Admins
        lifecycleScope.launch {
            try {
                adminsList = supabaseService.getAdmins() // Usa o método getAdmins
                val adminNames = adminsList.map { it.nome ?: "Admin sem nome" }
                adminAdapter.clear()
                adminAdapter.addAll(adminNames)
                adminAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                Toast.makeText(this@TelaRF17_2Activity, "Erro ao buscar admins: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    private fun setupPickers() {
        dateInput.setOnClickListener {
            showDatePicker()
        }
        timeInput.setOnClickListener {
            showTimePicker(timeInput, selectedDateTime) // Picker para a hora principal
        }
        startTimeInput.setOnClickListener {
            showTimePicker(startTimeInput, selectedStartTime, true) // Picker para hora início (opcional)
        }
        endTimeInput.setOnClickListener {
            showTimePicker(endTimeInput, selectedEndTime, true) // Picker para hora fim (opcional)
        }
    }

    private fun showDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            selectedDateTime.set(Calendar.YEAR, year)
            selectedDateTime.set(Calendar.MONTH, month)
            selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInput()
        }
        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker(targetInput: TextInputEditText, targetCalendar: Calendar, isOptional: Boolean = false) {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            targetCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            targetCalendar.set(Calendar.MINUTE, minute)
            targetCalendar.set(Calendar.SECOND, 0) // Zerar segundos
            updateTimeInput(targetInput, targetCalendar)
        }
        TimePickerDialog(
            this,
            timeSetListener,
            targetCalendar.get(Calendar.HOUR_OF_DAY),
            targetCalendar.get(Calendar.MINUTE),
            true // Formato 24 horas
        ).show()
    }

    private fun updateDateInput() {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale("pt", "BR"))
        dateInput.setText(sdf.format(selectedDateTime.time))
    }

    private fun updateTimeInput(input: TextInputEditText, calendar: Calendar) {
        val format = "HH:mm"
        val sdf = SimpleDateFormat(format, Locale("pt", "BR"))
        input.setText(sdf.format(calendar.time))
    }

    // Função para formatar data/hora para o padrão ISO 8601 que Supabase espera
    private fun formatDateTimeISO(calendar: Calendar): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        //sdf.timeZone = TimeZone.getTimeZone("UTC") // Opcional: Forçar UTC se necessário
        return sdf.format(calendar.time)
    }

    // Função para formatar apenas hora (HH:mm:ss) - para horario_inicio/fim
    private fun formatTime(calendar: Calendar): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    private fun salvarHorarioAtendimento() {
        // Validar campos obrigatórios
        if (selectedAdminId == null) {
            adminInput.error = "Selecione o admin responsável"
            return
        }
        if (selectedTipo == null) {
            tipoInput.error = "Selecione o tipo de atendimento"
            return
        }
        if (dateInput.text.isNullOrEmpty() || timeInput.text.isNullOrEmpty()) {
            Toast.makeText(this, "Selecione a data e a hora", Toast.LENGTH_SHORT).show()
            if (dateInput.text.isNullOrEmpty()) dateInput.error = "Obrigatório"
            if (timeInput.text.isNullOrEmpty()) timeInput.error = "Obrigatório"
            return
        }

        // Limpar erros
        adminInput.error = null
        tipoInput.error = null
        dateInput.error = null
        timeInput.error = null

        // Coletar dados opcionais
        val horarioInicioStr = if (startTimeInput.text.isNullOrEmpty()) null else formatTime(selectedStartTime)
        val horarioFimStr = if (endTimeInput.text.isNullOrEmpty()) null else formatTime(selectedEndTime)
        val diaSemanaStr = dayOfWeekInput.text.toString().trim().ifEmpty { null }

        // Criar objeto HorarioAtendimento
        val novoHorario = HorarioAtendimento(
            id = UUID.randomUUID().toString(), // Supabase pode gerar, mas enviar um é mais seguro
            admin_id = selectedAdminId!!,
            data_hora = formatDateTimeISO(selectedDateTime), // Formato ISO 8601
            tipo = selectedTipo!!,
            disponivel = true, // Valor padrão
            aluno_id = null, // Não definido na criação do horário
            aluno_nome = null, // Não definido na criação do horário
            horario_inicio = horarioInicioStr,
            horario_fim = horarioFimStr,
            dia_da_semana = diaSemanaStr
        )

        // Chamar API para salvar
        lifecycleScope.launch {
            try {
                // Certifique-se que o método criarHorarioAtendimento existe no SupabaseService
                val response = supabaseService.criarHorarioAtendimento(novoHorario)
                if (response.isNotEmpty()) {
                    Toast.makeText(this@TelaRF17_2Activity, "Horário de atendimento salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    finish() // Fechar a activity após salvar
                } else {
                    Toast.makeText(this@TelaRF17_2Activity, "Erro ao salvar horário.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TelaRF17_2Activity, "Erro ao salvar: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }
}

