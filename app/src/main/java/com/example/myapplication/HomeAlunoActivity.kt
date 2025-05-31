package com.example.myapplication

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeAlunoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_aluno)

        // Navegação

        // VLibras
        val webVLibras = findViewById<DraggableWebView>(R.id.webVLibras)
        val webSettings = webVLibras.settings
        webSettings.javaScriptEnabled = true
        webVLibras.setBackgroundColor(0x00000000) // Transparente

        // Coletar todos os textos visíveis da tela
        val rootView = findViewById<View>(android.R.id.content)
        val textoCompleto = coletarTextosDaTela(rootView)
        atualizarTextoVLibras(textoCompleto)

        // Habilitar movimentação do WebView
        webVLibras.setOnTouchListener(object : View.OnTouchListener {
            var downRawX = 0f
            var downRawY = 0f
            var dX = 0f
            var dY = 0f
            var isDragging = false
            val CLICK_DRAG_TOLERANCE = 10f

            override fun onTouch(view: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        downRawX = event.rawX
                        downRawY = event.rawY
                        dX = view.x - downRawX
                        dY = view.y - downRawY
                        isDragging = false
                        return false
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val newX = event.rawX + dX
                        val newY = event.rawY + dY
                        val maxX = (view.parent as View).width - view.width
                        val maxY = (view.parent as View).height - view.height
                        view.x = newX.coerceIn(0f, maxX.toFloat())
                        view.y = newY.coerceIn(0f, maxY.toFloat())

                        val deltaX = kotlin.math.abs(event.rawX - downRawX)
                        val deltaY = kotlin.math.abs(event.rawY - downRawY)
                        if (deltaX > CLICK_DRAG_TOLERANCE || deltaY > CLICK_DRAG_TOLERANCE) {
                            isDragging = true
                        }
                        return true
                    }

                    MotionEvent.ACTION_UP -> {
                        return isDragging
                    }

                    else -> return false
                }
            }
        })
    }

    // Coleta recursivamente todos os textos visíveis da tela
    private fun coletarTextosDaTela(view: View): String {
        val builder = StringBuilder()

        if (view is TextView && view.visibility == View.VISIBLE) {
            val texto = view.text.toString().trim()
            if (texto.isNotEmpty()) {
                builder.append(texto).append(". ")
            }
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                builder.append(coletarTextosDaTela(view.getChildAt(i)))
            }
        }

        return builder.toString()
    }

    // Gera o HTML invisível acessível ao VLibras
    private fun gerarHtmlVLibras(texto: String): String {
        return """
        <!DOCTYPE html>
        <html lang="pt-BR">
        <head>
            <meta charset="utf-8" />
            <style>
                body {
                    margin: 0;
                    padding: 0;
                    background: transparent;
                }
                p {
                    font-size: 16px;
                    color: transparent;
                    position: absolute;
                    pointer-events: auto;
                    user-select: text;
                    width: 100%;
                    height: auto;
                }
            </style>
        </head>
        <body>
            <p>$texto</p>
            <div vw class="enabled">
                <div vw-access-button class="active"></div>
                <div vw-plugin-wrapper>
                    <div class="vw-plugin-top-wrapper"></div>
                </div>
            </div>
            <script src="https://vlibras.gov.br/app/vlibras-plugin.js"></script>
            <script>
                new window.VLibras.Widget('https://vlibras.gov.br/app');
            </script>
        </body>
        </html>
    """.trimIndent()
    }

    // Atualiza o WebView com o novo texto para VLibras
    private fun atualizarTextoVLibras(texto: String) {
        val webVLibras = findViewById<DraggableWebView>(R.id.webVLibras)
        val html = gerarHtmlVLibras(texto)
        webVLibras.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
    }
}