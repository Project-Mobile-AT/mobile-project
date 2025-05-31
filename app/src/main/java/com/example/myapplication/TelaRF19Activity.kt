package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue // Importar TypedValue para converter dp em pixels
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // Importar lifecycleScope para corrotinas
import com.example.myapplication.data.SupabaseClient // Importar o client do Supabase
import com.example.myapplication.fragments.ChatbotPopupFragment
import com.example.myapplication.model.Informativo // Importar o modelo Informativo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch // Importar launch para corrotinas

class TelaRF19Activity : AppCompatActivity() {

    // Declaração das Views que serão manipuladas
    private lateinit var informativosContainer: LinearLayout // Container onde os cards serão adicionados
    private lateinit var progressBar: ProgressBar // Indicador de carregamento
    private lateinit var emptyErrorTextView: TextView // TextView para mensagens de erro ou lista vazia

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Define o layout da Activity (usando o XML modificado)
        setContentView(R.layout.activity_tela_rf19) // Usar o nome original do seu XML

        // --- Inicialização das Views --- //
        // Encontra as views no layout pelo ID
        informativosContainer = findViewById(R.id.ll_informativos_container)
        progressBar = findViewById(R.id.progressBar)
        emptyErrorTextView = findViewById(R.id.tv_empty_error)
        val addInform = findViewById<Button>(R.id.btnAdicionarInformativo)
        val fabChat = findViewById<FloatingActionButton>(R.id.fab_chat)
        val inicio = findViewById<ImageView>(R.id.homeIcon)
        val voltarIcon = findViewById<ImageView>(R.id.infoIcon) // Usando o ID do ícone de voltar

        // --- Configuração dos Listeners (Ações de Clique) --- //

        // Botão Voltar (Ícone no Topo)
        voltarIcon.setOnClickListener {
            finish() // Simplesmente fecha a activity atual
        }

        // Botão Adicionar Informativo
        addInform.setOnClickListener {
            val intent = Intent(this, TelaRF19_2Activity::class.java)
            startActivityForResult(intent, 1001)
        }


        // Botão Início (Rodapé)
        inicio.setOnClickListener {
            // Navega para a tela principal (TelaRF11Activity)
            val intent = Intent(this, TelaRF11Activity::class.java)
            startActivity(intent)
            finishAffinity() // Fecha todas as activities anteriores para evitar empilhamento
        }

        // Floating Action Button - Chat
        fabChat.setOnClickListener {
            // Mostra o popup do chatbot
            val chatbotPopup = ChatbotPopupFragment()
            chatbotPopup.show(supportFragmentManager, "ChatbotPopup")
        }

        // --- Carregamento dos Dados --- //
        // Chama a função para buscar os informativos do Supabase
        fetchInformativos()
    }

    // --- Função para Buscar Informativos no Supabase --- //
    private fun fetchInformativos() {
        // Mostra o indicador de carregamento e esconde outros elementos
        showLoading(true)

        // Inicia uma corrotina no escopo do ciclo de vida da Activity
        lifecycleScope.launch {
            try {
                // Chama a função getInformativos() definida na interface SupabaseService
                val informativos = SupabaseClient.service.getInformativos()

                // Esconde o indicador de carregamento após a busca
                showLoading(false)

                // Verifica se a lista de informativos não está vazia
                if (informativos.isNotEmpty()) {
                    // Se houver informativos, chama a função para exibi-los
                    displayInformativos(informativos)
                } else {
                    // Se a lista estiver vazia, mostra a mensagem de "nenhum informativo"
                    showEmptyError("Nenhum informativo encontrado.")
                }
            } catch (e: Exception) {
                // Em caso de erro na comunicação com o Supabase
                // Esconde o indicador de carregamento
                showLoading(false)
                // Loga o erro para depuração
                Log.e("SUPABASE_INFO", "Erro ao buscar informativos: ${e.message}", e)
                // Mostra uma mensagem de erro para o usuário
                showEmptyError("Erro ao carregar informativos. Tente novamente.")
            }
        }
    }

    // --- Função para Exibir os Informativos na Tela --- //
    private fun displayInformativos(informativos: List<Informativo>) {
        // Garante que o container de informativos esteja visível e a mensagem de erro/vazio escondida
        informativosContainer.visibility = View.VISIBLE
        emptyErrorTextView.visibility = View.GONE

        // Remove quaisquer views (cards antigos) que possam existir no container
        informativosContainer.removeAllViews()

        // Obtém o LayoutInflater para inflar o layout do card
        val inflater = LayoutInflater.from(this)

        // Converte 12dp para pixels para usar como margem (MAIS SIMPLES, SEM DEPENDER DE dimens.xml)
        val bottomMarginPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 12f, resources.displayMetrics
        ).toInt()

        // Itera sobre a lista de informativos recebida
        for (informativo in informativos) {
            // Infla o layout do card (item_informativo_card.xml)
            val cardView = inflater.inflate(R.layout.item_informativo_card, informativosContainer, false)

            // Encontra as Views dentro do card inflado
            val tituloTextView = cardView.findViewById<TextView>(R.id.tv_card_titulo) // Adapte os IDs se necessário
            val conteudoTextView = cardView.findViewById<TextView>(R.id.tv_card_conteudo) // Adapte os IDs se necessário
            val editButton = cardView.findViewById<ImageButton>(R.id.btn_card_edit) // Adapte os IDs se necessário

            // Preenche as Views com os dados do informativo atual
            tituloTextView.text = informativo.titulo
            conteudoTextView.text = informativo.conteudo ?: "" // Usa conteúdo ou string vazia se for nulo

            // Define a ação do botão Editar para este card específico
            editButton.setOnClickListener {
                // Cria um Intent para a tela de edição (TelaRF19_1Activity)
                val intent = Intent(this, TelaRF19_1Activity::class.java)
                // Passa os dados do informativo para a tela de edição
                intent.putExtra("INFORMATIVO_ID", informativo.id)
                intent.putExtra("INFORMATIVO_TITULO", informativo.titulo)
                intent.putExtra("INFORMATIVO_CONTEUDO", informativo.conteudo)
                // Inicia a Activity de edição
                startActivity(intent)
            }

            // Adiciona o card preenchido ao container LinearLayout
            informativosContainer.addView(cardView)

            // Adiciona um espaçamento (margem) abaixo de cada card programaticamente
            val layoutParams = cardView.layoutParams as LinearLayout.LayoutParams
            layoutParams.bottomMargin = bottomMarginPx // Usa o valor em pixels calculado
            cardView.layoutParams = layoutParams
        }
    }

    // --- Funções Auxiliares para Controle da UI --- //

    // Mostra/Esconde o ProgressBar e ajusta a visibilidade do conteúdo
    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        // Esconde o container e a mensagem de erro enquanto carrega
        if (isLoading) {
            informativosContainer.visibility = View.GONE
            emptyErrorTextView.visibility = View.GONE
        }
    }

    // Mostra a mensagem de erro ou lista vazia
    private fun showEmptyError(message: String) {
        informativosContainer.visibility = View.GONE // Esconde o container
        progressBar.visibility = View.GONE // Garante que o loading não está visível
        emptyErrorTextView.visibility = View.VISIBLE // Mostra a TextView
        emptyErrorTextView.text = message // Define o texto da mensagem
    }

    // --- Ciclo de Vida (Opcional: Recarregar ao voltar para a tela) --- //
    override fun onResume() {
        super.onResume()
        // Se desejar que os dados sejam atualizados toda vez que o usuário
        // voltar para esta tela, descomente a linha abaixo.
        // fetchInformativos()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            fetchInformativos() // Recarrega os informativos
        }
    }
}
