
package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient

import com.example.myapplication.data.SupabaseService
import com.example.myapplication.model.Agendamento
import com.example.myapplication.model.Aula
import com.example.myapplication.model.HorarioAtendimento // Certifique-se que este modelo existe
import com.example.myapplication.model.Usuario
import kotlinx.coroutines.launch
import java.util.UUID

class TelaRF17_2Activity : AppCompatActivity() {

    // Views do XML antigo (removidos ou comentados se não forem mais usados)
    // private lateinit var dateInput: TextInputEditText
    // private lateinit var startTimeInput: TextInputEditText
    // private lateinit var endTimeInput: TextInputEditText
    // private lateinit var slotsInput: TextInputEditText
    // private val calendar = Calendar.getInstance()

    // Views do XML novo
    private lateinit var tipoInput: AutoCompleteTextView
    private lateinit var alunoInput: AutoCompleteTextView
    private lateinit var adminInput: AutoCompleteTextView
    private lateinit var horarioInput: AutoCompleteTextView
    private lateinit var aulaInput: AutoCompleteTextView
    private lateinit var confirmadoInput: AutoCompleteTextView
    private lateinit var btnSalvar: Button
    private lateinit var backButton: ImageButton

    // Adapters e listas de dados
    private lateinit var alunoAdapter: ArrayAdapter<String>
    private lateinit var adminAdapter: ArrayAdapter<String>
    private lateinit var horarioAdapter: ArrayAdapter<String>
    private lateinit var aulaAdapter: ArrayAdapter<String>

    private var alunosList: List<Usuario> = emptyList()
    private var adminsList: List<Usuario> = emptyList()
    private var horariosList: List<HorarioAtendimento> = emptyList() // Use o tipo correto
    private var aulasList: List<Aula> = emptyList()

    // IDs selecionados
    private var selectedAlunoId: String? = null
    private var selectedAdminId: String? = null
    private var selectedHorarioId: String? = null
    private var selectedAulaId: String? = null
    private var selectedTipo: String? = null
    private var selectedConfirmado: Boolean? = null

    // Serviço da API
    private val supabaseService: SupabaseService = SupabaseClient.service // Usando o SupabaseClient fornecido

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf17_2)
        enableEdgeToEdge()

        // Inicializar views do novo XML
        tipoInput = findViewById(R.id.tipoInput)
        alunoInput = findViewById(R.id.alunoInput)
        adminInput = findViewById(R.id.adminInput)
        horarioInput = findViewById(R.id.horarioInput)
        aulaInput = findViewById(R.id.aulaInput)
        confirmadoInput = findViewById(R.id.confirmadoInput)
        btnSalvar = findViewById(R.id.btnSalvar)
        backButton = findViewById(R.id.backButton)

        // Configurar dropdowns estáticos
        setupStaticDropdowns()

        // Configurar dropdowns dinâmicos (buscar dados)
        setupDynamicDropdowns()

        // Configurar listeners de seleção
        setupSelectionListeners()

        // Botão voltar
        backButton.setOnClickListener {
            finish()
        }

        // Botão salvar
        btnSalvar.setOnClickListener {
            salvarAgendamento()
        }
    }

    private fun setupStaticDropdowns() {
        // Tipo de Agendamento
        val tipos = listOf("personal", "nutricionista", "aula")
        val tipoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, tipos)
        tipoInput.setAdapter(tipoAdapter)
        tipoInput.setOnItemClickListener { parent, _, position, _ ->
            selectedTipo = parent.adapter.getItem(position) as String
        }

        // Confirmado
        val confirmadoOptions = listOf("Sim", "Não")
        val confirmadoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, confirmadoOptions)
        confirmadoInput.setAdapter(confirmadoAdapter)
        confirmadoInput.setOnItemClickListener { parent, _, position, _ ->
            selectedConfirmado = (parent.adapter.getItem(position) as String == "Sim")
        }
    }

    private fun setupDynamicDropdowns() {
        // Inicializar adapters vazios
        alunoAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        adminAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        horarioAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())
        aulaAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mutableListOf<String>())

        alunoInput.setAdapter(alunoAdapter)
        adminInput.setAdapter(adminAdapter)
        horarioInput.setAdapter(horarioAdapter)
        aulaInput.setAdapter(aulaAdapter)

        // Buscar dados na coroutine
        lifecycleScope.launch {
            try {
                // Buscar Alunos
                alunosList = supabaseService.getAlunos()
                val alunoNames = alunosList.map { it.nome ?: "Aluno sem nome" } // Use um campo apropriado como nome
                alunoAdapter.clear()
                alunoAdapter.addAll(alunoNames)
                alunoAdapter.notifyDataSetChanged()

                // Buscar Admins
                adminsList = supabaseService.getAdmins()
                val adminNames = adminsList.map { it.nome ?: "Admin sem nome" } // Use um campo apropriado como nome
                adminAdapter.clear()
                adminAdapter.addAll(adminNames)
                adminAdapter.notifyDataSetChanged()

                // Buscar Horários
                horariosList = supabaseService.getHorarios() // Certifique-se que o método e modelo existem
                // Mapeie para uma representação de string significativa (ex: data e hora)
                val horarioStrings = horariosList.map { "${it.tipo}: ${it.data_hora}" } // Usa os campos reais do modelo HorarioAtendimento
                horarioAdapter.clear()
                horarioAdapter.addAll(horarioStrings)
                horarioAdapter.notifyDataSetChanged()

                // Buscar Aulas
                aulasList = supabaseService.getAulas()
                val aulaNames = aulasList.map { it.titulo ?: "Aula sem nome" }
                aulaAdapter.clear()
                aulaAdapter.addAll(aulaNames)
                aulaAdapter.notifyDataSetChanged()

            } catch (e: Exception) {
                // Tratar erro de busca
                Toast.makeText(this@TelaRF17_2Activity, "Erro ao buscar dados: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    private fun setupSelectionListeners() {
        alunoInput.setOnItemClickListener { _, _, position, _ ->
            selectedAlunoId = alunosList.getOrNull(position)?.id
        }
        adminInput.setOnItemClickListener { _, _, position, _ ->
            selectedAdminId = adminsList.getOrNull(position)?.id
        }
        horarioInput.setOnItemClickListener { _, _, position, _ ->
            selectedHorarioId = horariosList.getOrNull(position)?.id
        }
        aulaInput.setOnItemClickListener { _, _, position, _ ->
            selectedAulaId = aulasList.getOrNull(position)?.id
        }
    }

    private fun salvarAgendamento() {
        // Validar campos obrigatórios
        if (selectedTipo == null) {
            tipoInput.error = "Selecione o tipo"
            return
        }
        if (selectedAlunoId == null) {
            alunoInput.error = "Selecione o aluno"
            return
        }
        if (selectedConfirmado == null) {
            confirmadoInput.error = "Selecione se está confirmado"
            return
        }

        // Limpar erros
        tipoInput.error = null
        alunoInput.error = null
        confirmadoInput.error = null

        // Criar objeto Agendamento
        val novoAgendamento = Agendamento(
            id = UUID.randomUUID().toString(), // Gerar ID localmente ou deixar o Supabase gerar
            aluno_id = selectedAlunoId!!,
            admin_id = selectedAdminId, // Pode ser null
            horario_id = selectedHorarioId, // Pode ser null
            aula_id = selectedAulaId, // Pode ser null
            tipo = selectedTipo!!,
            confirmado = selectedConfirmado!!
        )

        // Chamar API para salvar
        lifecycleScope.launch {
            try {
                val response = supabaseService.criarAgendamento(novoAgendamento)
                if (response.isNotEmpty()) {
                    Toast.makeText(this@TelaRF17_2Activity, "Agendamento salvo com sucesso!", Toast.LENGTH_SHORT).show()
                    finish() // Fechar a activity após salvar
                } else {
                    Toast.makeText(this@TelaRF17_2Activity, "Erro ao salvar agendamento.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@TelaRF17_2Activity, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }
    }

    // Funções antigas de Date/Time Picker (removidas ou comentadas)
    /*
    private fun showDatePicker() { ... }
    private fun showTimePicker(input: TextInputEditText) { ... }
    private fun updateDateInput() { ... }
    private fun updateTimeInput(input: TextInputEditText) { ... }
    */
}

