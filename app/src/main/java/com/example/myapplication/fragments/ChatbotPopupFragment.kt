package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.launch

class ChatbotPopupFragment : DialogFragment() {

    private lateinit var editPergunta: EditText
    private lateinit var btnPerguntar: Button
    private lateinit var textResposta: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setStyle(STYLE_NORMAL, R.style.ChatbotDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chatbot_popup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editPergunta = view.findViewById(R.id.editPergunta)
        btnPerguntar = view.findViewById(R.id.btnPerguntar)
        textResposta = view.findViewById(R.id.textResposta)

        btnPerguntar.setOnClickListener {
            val pergunta = editPergunta.text.toString().trim()
            if (pergunta.isNotEmpty()) {
                perguntarGemini(pergunta)
            } else {
                textResposta.text = "Por favor, digite uma pergunta."
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    private fun perguntarGemini(pergunta: String) {
        val gm = GenerativeModel(
            modelName = "gemini-2.0-flash",
            apiKey = "" // ⚠️ Coloque em local seguro
        )

        textResposta.text = "Carregando resposta..."

        lifecycleScope.launch {
            try {
                val resp = gm.generateContent("responda em até 300 caractes a pergunta a seguir e se alguem perguntar do gabriel diga que ele está na academia:"+pergunta)
                val resposta = resp.text ?: "Não houve resposta."

                textResposta.text = resposta
            } catch (e: Exception) {
                textResposta.text = "Erro ao buscar resposta: ${e.message}"
                Log.e("GEMINI_ERROR", "Erro ao chamar API: ${e.message}", e)
            }
        }
    }
}
