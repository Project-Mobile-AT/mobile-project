package com.example.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.SupabaseClient
import com.example.myapplication.model.PlanoAlimentar
import com.example.myapplication.model.MedidasAluno
import kotlinx.coroutines.launch
import java.util.UUID
import android.util.Log

class FichaNutricionalActivity : AppCompatActivity() {
    private var planoId: String? = null
    private lateinit var editPlano: EditText
    private lateinit var btnSalvar: Button
    private lateinit var btnEditar: Button
    private lateinit var btnDeletar: Button
    private var usuarioId: String? = null
    private var medidasId: String? = null
    private lateinit var editPeso: EditText
    private lateinit var editAltura: EditText
    private lateinit var editIdade: EditText
    private lateinit var editBicepsEsquerdo: EditText
    private lateinit var editBicepsDireito: EditText
    private lateinit var editIMC: EditText
    private lateinit var editCintura: EditText
    private lateinit var btnSalvarMedidas: Button
    private lateinit var btnEditarMedidas: Button
    private lateinit var btnDeletarMedidas: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ficha_nutricional)
        enableEdgeToEdge()

        val nomeAluno = intent.getStringExtra("aluno_nome") ?: "Aluno"
        usuarioId = intent.getStringExtra("aluno_id")
        val tvTitulo = findViewById<TextView>(R.id.tvTituloFicha)
        tvTitulo.text = "Ficha $nomeAluno"

        editPlano = findViewById(R.id.editPlanoNutricional)
        btnSalvar = findViewById(R.id.btnSalvarPlano)
        btnEditar = findViewById(R.id.btnEditarPlano)
        btnDeletar = findViewById(R.id.btnDeletarPlano)
        editPeso = findViewById(R.id.editPeso)
        editAltura = findViewById(R.id.editAltura)
        editIdade = findViewById(R.id.editIdade)
        editBicepsEsquerdo = findViewById(R.id.editBicepsEsquerdo)
        editBicepsDireito = findViewById(R.id.editBicepsDireito)
        editIMC = findViewById(R.id.editIMC)
        editCintura = findViewById(R.id.editCintura)
        btnSalvarMedidas = findViewById(R.id.btnSalvar)
        btnEditarMedidas = findViewById(R.id.btnEditar)
        btnDeletarMedidas = findViewById(R.id.btnDeletar)

        buscarPlanoAlimentar()
        buscarMedidasAluno()

        btnSalvar.setOnClickListener {
            salvarPlanoAlimentar()
        }
        btnEditar.setOnClickListener {
            editarPlanoAlimentar()
        }
        btnDeletar.setOnClickListener {
            deletarPlanoAlimentar()
        }
        btnSalvarMedidas.setOnClickListener {
            salvarMedidasAluno()
        }
        btnEditarMedidas.setOnClickListener {
            editarMedidasAluno()
        }
        btnDeletarMedidas.setOnClickListener {
            deletarMedidasAluno()
        }
    }

    private fun buscarPlanoAlimentar() {
        val id = usuarioId ?: return
        lifecycleScope.launch {
            try {
                val planos = SupabaseClient.service.getPlanoAlimentarByUsuarioId("eq.$id")
                if (planos.isNotEmpty()) {
                    val plano = planos.first()
                    planoId = plano.id
                    editPlano.setText(plano.descricao ?: "")
                } else {
                    planoId = null
                    editPlano.setText("")
                }
            } catch (e: Exception) {
                Toast.makeText(this@FichaNutricionalActivity, "Erro ao buscar plano: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun salvarPlanoAlimentar() {
        val id = usuarioId ?: return
        val descricao = editPlano.text.toString().trim()
        if (descricao.isEmpty()) {
            Toast.makeText(this, "Preencha o plano alimentar", Toast.LENGTH_SHORT).show()
            return
        }
        val novoPlano = PlanoAlimentar(
            id = UUID.randomUUID().toString(),
            usuario_id = id,
            descricao = descricao,
            criado_em = null
        )
        lifecycleScope.launch {
            try {
                SupabaseClient.service.criarPlanoAlimentar(novoPlano)
                Toast.makeText(this@FichaNutricionalActivity, "Plano salvo!", Toast.LENGTH_SHORT).show()
                buscarPlanoAlimentar()
            } catch (e: Exception) {
                Toast.makeText(this@FichaNutricionalActivity, "Erro ao salvar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun editarPlanoAlimentar() {
        val id = planoId ?: return
        val descricao = editPlano.text.toString().trim()
        if (descricao.isEmpty()) {
            Toast.makeText(this, "Preencha o plano alimentar", Toast.LENGTH_SHORT).show()
            return
        }
        val planoEditado = PlanoAlimentar(
            id = id,
            usuario_id = usuarioId ?: return,
            descricao = descricao,
            criado_em = null
        )
        lifecycleScope.launch {
            try {
                SupabaseClient.service.atualizarPlanoAlimentar("eq.$id", planoEditado)
                Toast.makeText(this@FichaNutricionalActivity, "Plano atualizado!", Toast.LENGTH_SHORT).show()
                buscarPlanoAlimentar()
            } catch (e: Exception) {
                Toast.makeText(this@FichaNutricionalActivity, "Erro ao editar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deletarPlanoAlimentar() {
        val id = planoId ?: return
        lifecycleScope.launch {
            try {
                SupabaseClient.service.deletarPlanoAlimentar("eq.$id")
                Toast.makeText(this@FichaNutricionalActivity, "Plano deletado!", Toast.LENGTH_SHORT).show()
                buscarPlanoAlimentar()
            } catch (e: Exception) {
                Toast.makeText(this@FichaNutricionalActivity, "Erro ao deletar: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun buscarMedidasAluno() {
        val id = usuarioId ?: return
        lifecycleScope.launch {
            try {
                val medidasList = SupabaseClient.service.getMedidasByUsuarioId("eq.$id")
                if (medidasList.isNotEmpty()) {
                    val medidas = medidasList.first()
                    medidasId = medidas.id
                    editPeso.setText(medidas.peso?.toString() ?: "")
                    editAltura.setText(medidas.altura?.toString() ?: "")
                    editIdade.setText(medidas.idade?.toString() ?: "")
                    editBicepsEsquerdo.setText(medidas.biceps_esquerdo?.toString() ?: "")
                    editBicepsDireito.setText(medidas.biceps_direito?.toString() ?: "")
                    editIMC.setText(medidas.imc?.toString() ?: "")
                    editCintura.setText(medidas.cintura?.toString() ?: "")
                } else {
                    medidasId = null
                    editPeso.setText("")
                    editAltura.setText("")
                    editIdade.setText("")
                    editBicepsEsquerdo.setText("")
                    editBicepsDireito.setText("")
                    editIMC.setText("")
                    editCintura.setText("")
                }
            } catch (e: Exception) {
                Toast.makeText(this@FichaNutricionalActivity, "Erro ao buscar medidas: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun salvarMedidasAluno() {
        val id = usuarioId ?: return
        Toast.makeText(this, "usuario_id: $id", Toast.LENGTH_LONG).show()
        val uuid = try { UUID.randomUUID().toString() } catch (e: Exception) { null }
        if (uuid == null) {
            Toast.makeText(this, "Erro ao gerar UUID", Toast.LENGTH_LONG).show()
            return
        }
        Toast.makeText(this, "UUID gerado: $uuid", Toast.LENGTH_LONG).show()
        val medidas = MedidasAluno(
            id = uuid,
            usuario_id = id,
            peso = editPeso.text.toString().replace(",", ".").toDoubleOrNull(),
            altura = editAltura.text.toString().replace(",", ".").toDoubleOrNull(),
            idade = editIdade.text.toString().toIntOrNull(),
            biceps_esquerdo = editBicepsEsquerdo.text.toString().replace(",", ".").toDoubleOrNull(),
            biceps_direito = editBicepsDireito.text.toString().replace(",", ".").toDoubleOrNull(),
            imc = editIMC.text.toString().replace(",", ".").toDoubleOrNull(),
            cintura = editCintura.text.toString().replace(",", ".").toDoubleOrNull(),
            data_registro = null
        )
        Toast.makeText(this, "Payload: $medidas", Toast.LENGTH_LONG).show()
        Log.d("SUPABASE_DEBUG", "Tentando salvar medidas: $medidas")
        lifecycleScope.launch {
            try {
                val response = SupabaseClient.service.criarMedidasAluno(medidas)
                Log.d("SUPABASE_DEBUG", "Resposta do Supabase: $response")
                Toast.makeText(this@FichaNutricionalActivity, "Resposta do Supabase: $response", Toast.LENGTH_LONG).show()
                if (response.isNotEmpty()) {
                    Toast.makeText(this@FichaNutricionalActivity, "Medidas salvas!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@FichaNutricionalActivity, "Nenhuma medida retornada pelo Supabase.", Toast.LENGTH_LONG).show()
                }
                buscarMedidasAluno()
            } catch (e: Exception) {
                Log.e("SUPABASE_DEBUG", "Erro ao salvar medidas", e)
                Toast.makeText(this@FichaNutricionalActivity, "Erro ao salvar medidas: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun editarMedidasAluno() {
        val id = medidasId ?: return
        val medidas = MedidasAluno(
            id = id,
            usuario_id = usuarioId ?: return,
            peso = editPeso.text.toString().toDoubleOrNull(),
            altura = editAltura.text.toString().toDoubleOrNull(),
            idade = editIdade.text.toString().toIntOrNull(),
            biceps_esquerdo = editBicepsEsquerdo.text.toString().toDoubleOrNull(),
            biceps_direito = editBicepsDireito.text.toString().toDoubleOrNull(),
            imc = editIMC.text.toString().toDoubleOrNull(),
            cintura = editCintura.text.toString().toDoubleOrNull(),
            data_registro = null
        )
        lifecycleScope.launch {
            try {
                SupabaseClient.service.atualizarMedidasAluno("eq.$id", medidas)
                Toast.makeText(this@FichaNutricionalActivity, "Medidas atualizadas!", Toast.LENGTH_SHORT).show()
                buscarMedidasAluno()
            } catch (e: Exception) {
                Toast.makeText(this@FichaNutricionalActivity, "Erro ao editar medidas: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deletarMedidasAluno() {
        val id = medidasId ?: return
        lifecycleScope.launch {
            try {
                SupabaseClient.service.deletarMedidasAluno("eq.$id")
                Toast.makeText(this@FichaNutricionalActivity, "Medidas deletadas!", Toast.LENGTH_SHORT).show()
                buscarMedidasAluno()
            } catch (e: Exception) {
                Toast.makeText(this@FichaNutricionalActivity, "Erro ao deletar medidas: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
} 