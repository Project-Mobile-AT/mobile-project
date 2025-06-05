
package com.example.myapplication // Ajuste o pacote se necessário

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.HorarioAtendimento
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

// Adapter modificado para exibir horários com botão Editar
class HorariosAlunoAdapter(
    private var horarios: List<HorarioAtendimento>,
    private val onEditClick: (HorarioAtendimento) -> Unit // Mudou de onAgendar para onEditClick
) : RecyclerView.Adapter<HorariosAlunoAdapter.HorarioViewHolder>() {

    // ViewHolder atualizado para referenciar os elementos do novo layout
    inner class HorarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDataHora: TextView = itemView.findViewById(R.id.txt_data_hora)
        val txtTipo: TextView = itemView.findViewById(R.id.txt_tipo)
        val txtAdmin: TextView = itemView.findViewById(R.id.txt_admin) // Novo TextView
        val txtDiaHorarioOpcional: TextView = itemView.findViewById(R.id.txt_dia_horario_opcional) // Novo TextView
        val btnEditar: Button = itemView.findViewById(R.id.btn_editar) // Botão renomeado
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        // Infla o novo layout do item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_horario_edit, parent, false) // Usa item_horario_edit.xml
        return HorarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val horario = horarios[position]

        // Formata e exibe os dados principais
        holder.txtDataHora.text = formatDisplayDateTime(horario.data_hora)
        holder.txtTipo.text = "Tipo: ${horario.tipo}"
        // TODO: Idealmente, buscar o nome do admin pelo admin_id para exibir aqui
        holder.txtAdmin.text = "Admin ID: ${horario.admin_id}"

        // Monta e exibe a string de detalhes opcionais
        val detalhes = mutableListOf<String>()
        horario.dia_da_semana?.let { if (it.isNotBlank()) detalhes.add("Dia: $it") }
        horario.horario_inicio?.let { if (it.isNotBlank()) detalhes.add("Início: $it") }
        horario.horario_fim?.let { if (it.isNotBlank()) detalhes.add("Fim: $it") }
        holder.txtDiaHorarioOpcional.text = if (detalhes.isNotEmpty()) detalhes.joinToString(" | ") else ""
        holder.txtDiaHorarioOpcional.visibility = if (detalhes.isNotEmpty()) View.VISIBLE else View.GONE

        // Configura o clique no botão Editar
        holder.btnEditar.setOnClickListener {
            onEditClick(horario) // Chama a função lambda passada pela Activity
        }
    }

    override fun getItemCount(): Int = horarios.size

    // Função para atualizar a lista (sem alterações)
    fun updateList(newList: List<HorarioAtendimento>) {
        horarios = newList
        notifyDataSetChanged()
    }

    // Função auxiliar para formatar a data/hora ISO para exibição amigável
    private fun formatDisplayDateTime(isoDateTime: String?): String {
        if (isoDateTime == null) return "Data/Hora: N/A"
        // Ajuste o padrão de entrada se o formato do Supabase for diferente
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale("pt", "BR"))
        return try {
            val date = inputFormat.parse(isoDateTime)
            if (date != null) outputFormat.format(date) else "Data/Hora inválida"
        } catch (e: ParseException) {
            // Tentar formato sem timezone se o primeiro falhar
            try {
                val inputFormatFallback = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val date = inputFormatFallback.parse(isoDateTime)
                if (date != null) outputFormat.format(date) else "Data/Hora inválida"
            } catch (e2: ParseException) {
                isoDateTime // Retorna a string original se ambos falharem
            }
        }
    }
}

