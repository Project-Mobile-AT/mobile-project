
package com.example.myapplication // Ajuste o pacote se necessário

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.data.SupabaseService
import com.example.myapplication.model.HorarioAtendimento
import com.example.myapplication.model.Usuario
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class TelaRF17_4Activity : AppCompatActivity() {

    // Views do XML (para editar HorarioAtendimento)
    private lateinit var adminInput: AutoCompleteTextView
    private lateinit var tipoInput: AutoCompleteTextView
    private lateinit var dateInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText // Para a hora principal (data_hora)
    private lateinit var startTimeInput: TextInputEditText // Opcional
    private lateinit var endTimeInput: TextInputEditText // Opcional
    private lateinit var dayOfWeekInput: TextInputEditText // Opcional
    private lateinit var btnSalvar: Button
    private lateinit var btnDeletar: Button
    private lateinit var btnVoltar: ImageButton

    // Adapters e listas de dados
    private lateinit var adminAdapter: ArrayAdapter<String>
    private var adminsList: List<Usuario> = emptyList()

    // Variáveis para guardar seleções e estado
    private var selectedAdminId: String? = null
    private var selectedTipo: String? = null
    private val selectedDateTime = Calendar.getInstance() // Guarda data e hora combinadas
    private val selectedStartTime = Calendar.getInstance() // Guarda hora de início opcional
    private val selectedEndTime = Calendar.getInstance() // Guarda hora de fim opcional
    private var currentHorario: HorarioAtendimento? = null // Guarda o horário carregado
    private var horarioId: String? = null

    // Serviço da API
    private val supabaseService: SupabaseService = SupabaseClient.service

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_telarf17_4) // Usa o layout de edição
        enableEdgeToEdge()

        // Obter o ID do horário da Intent
        horarioId = intent.getStringExtra(TelaRF17_3Activity.EXTRA_HORARIO_ID)

        if (horarioId == null) {
            Toast.makeText(this, "ID do horário não encontrado!", Toast.LENGTH_LONG).show()
            finish() // Fecha a activity se não houver ID
            return
        }

        // Inicializar views
        adminInput = findViewById(R.id.adminInput)
        tipoInput = findViewById(R.id.tipoInput)
        dateInput = findViewById(R.id.dateInput)
        timeInput = findViewById(R.id.timeInput)
        startTimeInput = findViewById(R.id.startTimeInput)
        endTimeInput = findViewById(R.id.endTimeInput)
        dayOfWeekInput = findViewById(R.id.dayOfWeekInput)
        btnSalvar = findViewById(R.id.btnSalvar)
        btnDeletar = findViewById(R.id.btnDeletar)
        btnVoltar = findViewById(R.id.btnVoltar) // ID do botão voltar no XML

        // Configurar dropdowns estáticos e dinâmicos
        setupTipoDropdown()
        setupAdminDropdownAndLoadData() // Combina busca de admin e dados do horário

        // Configurar Date/Time Pickers
        setupPickers()

        // Botão voltar
        btnVoltar.setOnClickListener {
            finish() // Apenas fecha a tela atual
        }

        // Botão salvar
        btnSalvar.setOnClickListener {
            salvarAlteracoes()
        }

        // Botão deletar
        btnDeletar.setOnClickListener {
            confirmarDelecao()
        }
    }

    private fun setupTipoDropdown() {
        val tipos = listOf("personal", "nutricionista")
        val tipoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipos)
        tipoInput.setAdapter(tipoAdapter)
        tipoInput.setOnItemClickListener { _, _, position, _ ->
            selectedTipo = tipoAdapter.getItem(position)
        }
    }

    // Busca admins e depois carrega os dados do horário específico
    private fun setupAdminDropdownAndLoadData() {
        adminAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        adminInput.setAdapter(adminAdapter)

        adminInput.setOnItemClickListener { _, _, position, _ ->
            selectedAdminId = adminsList.getOrNull(position)?.id
        }

        lifecycleScope.launch {
            try {
                // 1. Buscar Admins
                adminsList = supabaseService.getAdmins()
                val adminNames = adminsList.map { it.nome ?: "Admin sem nome" }
                adminAdapter.clear()
                adminAdapter.addAll(adminNames)
                adminAdapter.notifyDataSetChanged()

                // 2. Buscar dados do horário específico (APÓS buscar admins)
                loadHorarioData(horarioId!!)

            } catch (e: Exception) {
                Toast.makeText(this@TelaRF17_4Activity, "Erro ao carregar dados iniciais: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
                finish() // Fecha se não conseguir carregar dados
            }
        }
    }

    // Carrega os dados do horário pelo ID e preenche o formulário
    private fun loadHorarioData(id: String) {
        lifecycleScope.launch {
            try {
                // Usa o método getHorarioById
                val response = supabaseService.getHorarioById(id = "eq.$id")
                if (response.isNotEmpty()) {
                    currentHorario = response[0]
                    populateForm(currentHorario!!)
                } else {
                    Toast.makeText(this@TelaRF17_4Activity, "Horário não encontrado.", Toast.LENGTH_LONG).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TelaRF17_4Activity, "Erro ao buscar horário: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
                finish()
            }
        }
    }

    // Preenche os campos do formulário com os dados do HorarioAtendimento
    private fun populateForm(horario: HorarioAtendimento) {
        // Seleciona o Admin no dropdown
        selectedAdminId = horario.admin_id
        val adminIndex = adminsList.indexOfFirst { it.id == selectedAdminId }
        if (adminIndex != -1) {
            adminInput.setText(adminAdapter.getItem(adminIndex), false)
        }

        // Seleciona o Tipo no dropdown
        selectedTipo = horario.tipo
        tipoInput.setText(selectedTipo, false)

        // Preenche Data e Hora principal (data_hora)
        parseAndSetDateTime(horario.data_hora, selectedDateTime, dateInput, timeInput)

        // Preenche Horário Início (opcional)
        parseAndSetTime(horario.horario_inicio, selectedStartTime, startTimeInput)

        // Preenche Horário Fim (opcional)
        parseAndSetTime(horario.horario_fim, selectedEndTime, endTimeInput)

        // Preenche Dia da Semana (opcional)
        dayOfWeekInput.setText(horario.dia_da_semana ?: "")
    }

    // Funções auxiliares para parsear e definir datas/horas nos campos e Calendars
    private fun parseAndSetDateTime(isoDateTime: String?, targetCalendar: Calendar, dateEditText: TextInputEditText, timeEditText: TextInputEditText) {
        if (isoDateTime == null) return
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        try {
            val date = inputFormat.parse(isoDateTime)
            if (date != null) {
                targetCalendar.time = date
                updateDateInput(dateEditText, targetCalendar)
                updateTimeInput(timeEditText, targetCalendar)
            }
        } catch (e: ParseException) {
            // Tentar formato sem timezone
            try {
                val inputFormatFallback = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = inputFormatFallback.parse(isoDateTime)
                if (date != null) {
                    targetCalendar.time = date
                    updateDateInput(dateEditText, targetCalendar)
                    updateTimeInput(timeEditText, targetCalendar)
                }
            } catch (e2: ParseException) {
                e2.printStackTrace()
                Toast.makeText(this, "Formato de data/hora inválido: $isoDateTime", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun parseAndSetTime(timeString: String?, targetCalendar: Calendar, timeEditText: TextInputEditText) {
        if (timeString == null) {
            timeEditText.setText("")
            return
        }
        val inputFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        try {
            val date = inputFormat.parse(timeString)
            if (date != null) {
                val tempCal = Calendar.getInstance()
                tempCal.time = date
                targetCalendar.set(Calendar.HOUR_OF_DAY, tempCal.get(Calendar.HOUR_OF_DAY))
                targetCalendar.set(Calendar.MINUTE, tempCal.get(Calendar.MINUTE))
                targetCalendar.set(Calendar.SECOND, tempCal.get(Calendar.SECOND))
                updateTimeInput(timeEditText, targetCalendar)
            }
        } catch (e: ParseException) {
            e.printStackTrace()
            timeEditText.setText("") // Limpa se o formato for inválido
            Toast.makeText(this, "Formato de hora inválido: $timeString", Toast.LENGTH_SHORT).show()
        }
    }

    // --- Funções de Date/Time Picker (semelhantes à TelaRF17_2) ---
    private fun setupPickers() {
        dateInput.setOnClickListener { showDatePicker() }
        timeInput.setOnClickListener { showTimePicker(timeInput, selectedDateTime) }
        startTimeInput.setOnClickListener { showTimePicker(startTimeInput, selectedStartTime, true) }
        endTimeInput.setOnClickListener { showTimePicker(endTimeInput, selectedEndTime, true) }
    }

    private fun showDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            selectedDateTime.set(Calendar.YEAR, year)
            selectedDateTime.set(Calendar.MONTH, month)
            selectedDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInput(dateInput, selectedDateTime)
        }
        DatePickerDialog(this, dateSetListener, selectedDateTime.get(Calendar.YEAR), selectedDateTime.get(Calendar.MONTH), selectedDateTime.get(Calendar.DAY_OF_MONTH)).show()
    }

    private fun showTimePicker(targetInput: TextInputEditText, targetCalendar: Calendar, isOptional: Boolean = false) {
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            targetCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            targetCalendar.set(Calendar.MINUTE, minute)
            targetCalendar.set(Calendar.SECOND, 0)
            updateTimeInput(targetInput, targetCalendar)
        }
        TimePickerDialog(this, timeSetListener, targetCalendar.get(Calendar.HOUR_OF_DAY), targetCalendar.get(Calendar.MINUTE), true).show()
    }

    private fun updateDateInput(input: TextInputEditText, calendar: Calendar) {
        val format = "dd/MM/yyyy"
        val sdf = SimpleDateFormat(format, Locale("pt", "BR"))
        input.setText(sdf.format(calendar.time))
    }

    private fun updateTimeInput(input: TextInputEditText, calendar: Calendar) {
        val format = "HH:mm"
        val sdf = SimpleDateFormat(format, Locale("pt", "BR"))
        input.setText(sdf.format(calendar.time))
    }

    // --- Funções de Formatação para Supabase (semelhantes à TelaRF17_2) ---
    private fun formatDateTimeISO(calendar: Calendar): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    private fun formatTime(calendar: Calendar): String {
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        return sdf.format(calendar.time)
    }

    // --- Lógica de Salvar e Deletar (a ser implementada nos próximos passos) ---
    private fun salvarAlteracoes() {
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

        // Coletar dados opcionais atualizados
        val horarioInicioStr = if (startTimeInput.text.isNullOrEmpty()) null else formatTime(selectedStartTime)
        val horarioFimStr = if (endTimeInput.text.isNullOrEmpty()) null else formatTime(selectedEndTime)
        val diaSemanaStr = dayOfWeekInput.text.toString().trim().ifEmpty { null }

        // Criar objeto HorarioAtendimento com os dados ATUALIZADOS
        // Mantém o ID original e outros campos não editáveis (como aluno_id, aluno_nome, disponivel)
        val horarioAtualizado = currentHorario?.copy(
            admin_id = selectedAdminId!!,
            data_hora = formatDateTimeISO(selectedDateTime),
            tipo = selectedTipo!!,
            horario_inicio = horarioInicioStr,
            horario_fim = horarioFimStr,
            dia_da_semana = diaSemanaStr
            // Manter o valor original de 'disponivel', 'aluno_id', 'aluno_nome' se não forem editáveis aqui
            // disponivel = currentHorario.disponivel,
            // aluno_id = currentHorario.aluno_id,
            // aluno_nome = currentHorario.aluno_nome
        )

        if (horarioAtualizado == null) {
            Toast.makeText(this, "Erro ao preparar dados para salvar.", Toast.LENGTH_SHORT).show()
            return
        }

        // Chamar API para atualizar (PATCH)
        lifecycleScope.launch {
            try {
                // Usa o método atualizarHorarioAtendimento
                // Passa o filtro "eq.ID" e o objeto atualizado
                val response = supabaseService.atualizarHorarioAtendimentoo(
                    id = "eq.${horarioId!!}", // Filtro pelo ID
                    horarioUpdate = horarioAtualizado
                )
                if (response.isNotEmpty()) {
                    Toast.makeText(this@TelaRF17_4Activity, "Alterações salvas com sucesso!", Toast.LENGTH_SHORT).show()
                    finish() // Fecha a activity após salvar
                } else {
                    Toast.makeText(this@TelaRF17_4Activity, "Erro ao salvar alterações.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TelaRF17_4Activity, "Erro ao salvar: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    private fun confirmarDelecao() {
        if (horarioId == null) {
            Toast.makeText(this, "ID do horário inválido para deletar.", Toast.LENGTH_SHORT).show()
            return
        }

        // Exibir diálogo de confirmação
        AlertDialog.Builder(this)
            .setTitle("Confirmar Exclusão")
            .setMessage("Tem certeza que deseja deletar este horário de atendimento? Esta ação não pode ser desfeita.")
            .setPositiveButton("Deletar") { dialog, _ ->
                // Usuário confirmou, chamar a função de deleção
                deletarHorario(horarioId!!)
                dialog.dismiss()
            }
            .setNegativeButton("Cancelar") { dialog, _ ->
                // Usuário cancelou
                dialog.dismiss()
            }
            .show()
    }

    // Função que efetivamente chama a API para deletar
    private fun deletarHorario(id: String) {
        lifecycleScope.launch {
            try {
                // Usa o método deletarHorarioAtendimento
                // Passa o filtro "eq.ID"
                val response = supabaseService.deletarHorarioAtendimento(id = "eq.$id")

                // Verificar se a deleção foi bem-sucedida (código 204 No Content)
                if (response.isSuccessful && response.code() == 204) {
                    Toast.makeText(this@TelaRF17_4Activity, "Horário deletado com sucesso!", Toast.LENGTH_SHORT).show()
                    finish() // Fecha a activity após deletar
                } else {
                    // Tentar obter mensagem de erro do corpo, se houver
                    val errorBody = response.errorBody()?.string() ?: "Erro desconhecido"
                    Toast.makeText(this@TelaRF17_4Activity, "Erro ao deletar horário: ${response.code()} - $errorBody", Toast.LENGTH_LONG).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TelaRF17_4Activity, "Erro ao deletar: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }
}


