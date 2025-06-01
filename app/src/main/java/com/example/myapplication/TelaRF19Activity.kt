package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.fragments.ChatbotPopupFragment
import com.example.myapplication.model.Informativo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class TelaRF19Activity : AppCompatActivity() {

    private lateinit var informativosContainer: LinearLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyErrorTextView: TextView

    private lateinit var editLauncher: ActivityResultLauncher<Intent>
    private lateinit var createLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tela_rf19)

        informativosContainer = findViewById(R.id.ll_informativos_container)
        progressBar = findViewById(R.id.progressBar)
        emptyErrorTextView = findViewById(R.id.tv_empty_error)

        val addInform = findViewById<Button>(R.id.btnAdicionarInformativo)
        val fabChat = findViewById<FloatingActionButton>(R.id.fab_chat)
        val inicio = findViewById<ImageView>(R.id.homeIcon)
        val voltarIcon = findViewById<ImageView>(R.id.infoIcon)

        // 🟣 Register Activity Result Launchers
        editLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                fetchInformativos()
            }
        }

        createLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                fetchInformativos()
            }
        }

        voltarIcon.setOnClickListener { finish() }

        addInform.setOnClickListener {
            val intent = Intent(this, TelaRF19_2Activity::class.java)
            createLauncher.launch(intent)
        }

        inicio.setOnClickListener {
            startActivity(Intent(this, TelaRF11Activity::class.java))
            finishAffinity()
        }

        fabChat.setOnClickListener {
            val chatbotPopup = ChatbotPopupFragment()
            chatbotPopup.show(supportFragmentManager, "ChatbotPopup")
        }

        fetchInformativos()
    }

    private fun fetchInformativos() {
        showLoading(true)

        lifecycleScope.launch {
            try {
                val informativos = SupabaseClient.service.getInformativos()
                showLoading(false)

                if (informativos.isNotEmpty()) {
                    displayInformativos(informativos)
                } else {
                    showEmptyError("Nenhum informativo encontrado.")
                }
            } catch (e: Exception) {
                showLoading(false)
                Log.e("SUPABASE_INFO", "Erro ao buscar informativos: ${e.message}", e)
                showEmptyError("Erro ao carregar informativos. Tente novamente.")
            }
        }
    }

    private fun displayInformativos(informativos: List<Informativo>) {
        informativosContainer.visibility = View.VISIBLE
        emptyErrorTextView.visibility = View.GONE
        informativosContainer.removeAllViews()

        val inflater = LayoutInflater.from(this)
        val bottomMarginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 12f, resources.displayMetrics
        ).toInt()

        for (informativo in informativos) {
            val cardView = inflater.inflate(R.layout.item_informativo_card, informativosContainer, false)

            val tituloTextView = cardView.findViewById<TextView>(R.id.tv_card_titulo)
            val conteudoTextView = cardView.findViewById<TextView>(R.id.tv_card_conteudo)
            val editButton = cardView.findViewById<ImageButton>(R.id.btn_card_edit)

            tituloTextView.text = informativo.titulo
            conteudoTextView.text = informativo.conteudo ?: ""

            editButton.setOnClickListener {
                val intent = Intent(this, TelaRF19_1Activity::class.java).apply {
                    putExtra("INFORMATIVO_ID", informativo.id)
                    putExtra("INFORMATIVO_TITULO", informativo.titulo)
                    putExtra("INFORMATIVO_CONTEUDO", informativo.conteudo)
                }
                editLauncher.launch(intent)
            }

            informativosContainer.addView(cardView)

            val layoutParams = cardView.layoutParams as LinearLayout.LayoutParams
            layoutParams.bottomMargin = bottomMarginPx
            cardView.layoutParams = layoutParams
        }
    }

    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        if (isLoading) {
            informativosContainer.visibility = View.GONE
            emptyErrorTextView.visibility = View.GONE
        }
    }

    private fun showEmptyError(message: String) {
        informativosContainer.visibility = View.GONE
        progressBar.visibility = View.GONE
        emptyErrorTextView.visibility = View.VISIBLE
        emptyErrorTextView.text = message
    }
}
