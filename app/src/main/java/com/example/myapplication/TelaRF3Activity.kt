package com.example.myapplication

import android.content.Context // Adicionar este import!
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.data.SupabaseService
import com.example.myapplication.fragments.AcessibilidadePopup
import com.example.myapplication.fragments.BottomNavFragment
import com.example.myapplication.fragments.ChatbotPopupFragment
import com.example.myapplication.model.Informativo
import com.example.myapplication.model.Presenca
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.UUID

class TelaRF3Activity : AppCompatActivity() {

    private lateinit var supabaseService: SupabaseService
    private var currentUserId: String? = null

    private lateinit var userNameDisplayTextView: TextView
    private lateinit var streakValueTextView: TextView
    private lateinit var treinoNameTextView: TextView
    private lateinit var comunicadosContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tela_rf3)

        // Inicialize o serviço Supabase (Retrofit)
        supabaseService = SupabaseClient.service

        // --- OBTER ID DO USUÁRIO LOGADO ---
        // Pegue o ID do SharedPreferences, usando a mesma chave que você usou para salvar.
        currentUserId = getSharedPreferences("app_prefs", MODE_PRIVATE).getString("logged_in_user_id", null)

        if (currentUserId == null) {
            Toast.makeText(this, "Erro: ID do usuário não encontrado. Por favor, faça login novamente.", Toast.LENGTH_LONG).show()
            // Limpe o ID antigo se houver para evitar loops
            getSharedPreferences("app_prefs", MODE_PRIVATE).edit().remove("logged_in_user_id").apply()
            val intent = Intent(this, TelaRF1Activity::class.java)
            startActivity(intent)
            finish()
            return
        }

        // --- Inicializar Referências da UI ---
        userNameDisplayTextView = findViewById(R.id.nome_usuario_text_view)
        streakValueTextView = findViewById(R.id.txt_streak_value)
        treinoNameTextView = findViewById(R.id.txt_treino_do_dia)
        comunicadosContainer = findViewById(R.id.comunicados_container)

        val perfilImageView = findViewById<ImageView>(R.id.ic_per)
        val menuButtonImageView = findViewById<ImageView>(R.id.imageView)
        val buttonMinhaFicha = findViewById<Button>(R.id.btn_minhaFicha)
        val btn_checkin = findViewById<Button>(R.id.btn_checkin)
        val fabChat = findViewById<FloatingActionButton>(R.id.fab_chat)
        val fabAcessibilidade = findViewById<FloatingActionButton>(R.id.fab_acessibilidade)
        val avatarImageView = findViewById<ImageView>(R.id.teste)

        // --- Configurar Listeners de Click ---
        perfilImageView.setOnClickListener {
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
            val acessibilidadePopup = AcessibilidadePopup()
            acessibilidadePopup.show(supportFragmentManager, "AcessibilidadePopup")
        }

        menuButtonImageView.setOnClickListener { view ->
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
                        // Limpa o ID do usuário salvo no SharedPreferences
                        getSharedPreferences("app_prefs", MODE_PRIVATE).edit().remove("logged_in_user_id").apply()
                        val intent = Intent(this, TelaRF1Activity::class.java)
                        startActivity(intent)
                        finish()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        avatarImageView.setOnClickListener{
            val intent = Intent(this, HomeAlunoActivity::class.java)
            startActivity(intent)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.container_bottom_nav, BottomNavFragment())
            .commit()

        // --- Chamadas para carregar dados do Supabase ---
        loadUserData()
        loadStreakData()
        loadTreinoData()
        loadInformativos()

        // --- Lógica para o botão CHECK-IN ---
        btn_checkin.setOnClickListener {
            performCheckIn()
        }
    }

    // --- Funções para Carregar Dados do Supabase e Atualizar a UI ---

    private fun loadUserData() {
        currentUserId?.let { userId ->
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // Adicionar "eq." ao ID para o filtro, como você faz em getUsuarioById
                    val users = supabaseService.getUsuarioById(id = "eq.$userId")
                    withContext(Dispatchers.Main) {
                        if (users.isNotEmpty()) {
                            val user = users.first()
                            userNameDisplayTextView.text = user.nome
                        } else {
                            Toast.makeText(this@TelaRF3Activity, "Usuário não encontrado.", Toast.LENGTH_SHORT).show()
                            userNameDisplayTextView.text = "Usuário"
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("TelaRF3Activity", "Erro ao carregar dados do usuário: ${e.message}", e)
                        Toast.makeText(this@TelaRF3Activity, "Erro ao carregar perfil.", Toast.LENGTH_SHORT).show()
                        userNameDisplayTextView.text = "Erro!"
                    }
                }
            }
        }
    }

    private fun loadStreakData() {
        currentUserId?.let { userId ->
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // Adicionar "eq." ao ID para o filtro
                    val streaks = supabaseService.getStreakByUserId(userId = "eq.$userId")
                    withContext(Dispatchers.Main) {
                        if (streaks.isNotEmpty()) {
                            val streak = streaks.first()
                            streakValueTextView.text = "${streak.completado} dias"
                        } else {
                            streakValueTextView.text = "0 dias"
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("TelaRF3Activity", "Erro ao carregar streak: ${e.message}", e)
                        Toast.makeText(this@TelaRF3Activity, "Erro ao carregar streak.", Toast.LENGTH_SHORT).show()
                        streakValueTextView.text = "Erro"
                    }
                }
            }
        }
    }

    private fun loadTreinoData() {
        currentUserId?.let { userId ->
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // Adicionar "eq." ao ID para o filtro
                    val treinos = supabaseService.getTreinoByUserId(userId = "eq.$userId", "criado_em.desc", 1)
                    withContext(Dispatchers.Main) {
                        if (treinos.isNotEmpty()) {
                            val treino = treinos.first()
                            treinoNameTextView.text = "Treino: ${treino.id}"
                        } else {
                            treinoNameTextView.text = "Nenhum treino disponível hoje."
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("TelaRF3Activity", "Erro ao carregar treino: ${e.message}", e)
                        Toast.makeText(this@TelaRF3Activity, "Erro ao carregar treino do dia.", Toast.LENGTH_SHORT).show()
                        treinoNameTextView.text = "Erro ao carregar"
                    }
                }
            }
        }
    }

    private fun loadInformativos() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val informativos = supabaseService.getInformativos()
                withContext(Dispatchers.Main) {
                    updateInformativosUI(informativos)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("TelaRF3Activity", "Erro ao carregar informativos: ${e.message}", e)
                    Toast.makeText(this@TelaRF3Activity, "Erro ao carregar comunicados.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateInformativosUI(informativos: List<Informativo>) {
        comunicadosContainer.removeAllViews()

        if (informativos.isEmpty()) {
            val noDataText = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                text = "Nenhum comunicado disponível no momento."
                gravity = Gravity.CENTER_HORIZONTAL
                setPadding(0, dpToPx(16), 0, 0)
            }
            comunicadosContainer.addView(noDataText)
            return
        }

        informativos.forEachIndexed { index, informativo ->
            val cardView = layoutInflater.inflate(R.layout.item_comunicado_card, comunicadosContainer, false) as LinearLayout

            val titleTextView = cardView.findViewById<TextView>(R.id.card_title)
            val contentTextView = cardView.findViewById<TextView>(R.id.card_content)
            val dateTextView = cardView.findViewById<TextView>(R.id.card_date)

            titleTextView.text = informativo.titulo
            contentTextView.text = informativo.conteudo

            dateTextView.text = formatSupabaseTimestampToDate(informativo.criado_em)

            val backgroundColorRes = if (index % 2 == 0) R.drawable.rounded_red_light else R.drawable.rounded_blue_light
            val titleColor = if (index % 2 == 0) Color.parseColor("#FF0000") else Color.parseColor("#001199")

            cardView.setBackgroundResource(backgroundColorRes)
            titleTextView.setTextColor(titleColor)

            comunicadosContainer.addView(cardView)
        }
    }

    private fun performCheckIn() {
        currentUserId?.let { userId ->
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val presenca = Presenca(
                        id = UUID.randomUUID().toString(),
                        aula_id = "checkin_geral",
                        usuario_id = userId,
                        checkin = true
                    )
                    val result = supabaseService.createPresenca(presenca)
                    withContext(Dispatchers.Main) {
                        if (result.isNotEmpty()) {
                            Toast.makeText(this@TelaRF3Activity, "Check-in realizado com sucesso!", Toast.LENGTH_SHORT).show()
                            loadStreakData()
                        } else {
                            Toast.makeText(this@TelaRF3Activity, "Falha ao realizar check-in.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e("TelaRF3Activity", "Erro ao realizar check-in: ${e.message}", e)
                        Toast.makeText(this@TelaRF3Activity, "Erro ao realizar check-in: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        } ?: run {
            Toast.makeText(this, "Erro: ID do usuário não disponível para check-in.", Toast.LENGTH_SHORT).show()
        }
    }

    // --- Funções Utilitárias ---
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    private fun formatSupabaseTimestampToDate(timestamp: String?): String {
        if (timestamp.isNullOrEmpty()) return ""

        val inputFormats = listOf(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.US),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.US),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.US),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        )

        val utcTimeZone = TimeZone.getTimeZone("UTC")
        inputFormats.forEach { it.timeZone = utcTimeZone }

        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR"))

        var parsedDate: Date? = null
        for (format in inputFormats) {
            try {
                parsedDate = format.parse(timestamp)
                if (parsedDate != null) {
                    break
                }
            } catch (e: Exception) {
                Log.d("TelaRF3Activity", "Falha ao parsear '$timestamp' com formato '${format.toPattern()}': ${e.message}")
            }
        }

        return parsedDate?.let { outputFormat.format(it) } ?: timestamp
    }
}