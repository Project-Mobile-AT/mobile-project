package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.HorarioNutricionista

class HorariosNutricionistaAdapter(
    private var horarios: List<HorarioNutricionista>,
    private val onEditar: (HorarioNutricionista) -> Unit,
    private val onDeletar: (HorarioNutricionista) -> Unit
) : RecyclerView.Adapter<HorariosNutricionistaAdapter.HorarioViewHolder>() {

    inner class HorarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDataHora: TextView = itemView.findViewById(R.id.txt_data_hora)
        val txtDisponivel: TextView = itemView.findViewById(R.id.txt_disponivel)
        val txtAluno: TextView = itemView.findViewById(R.id.txt_aluno)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btn_editar)
        val btnDeletar: ImageButton = itemView.findViewById(R.id.btn_deletar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horario_nutricionista, parent, false)
        return HorarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val horario = horarios[position]
        holder.txtDataHora.text = horario.data_hora
        holder.txtDisponivel.text = if (horario.disponivel) "Disponível" else "Indisponível"
        holder.txtAluno.text = "Aluno: ${horario.aluno_nome ?: "-"}"
        holder.btnEditar.setOnClickListener { onEditar(horario) }
        holder.btnDeletar.setOnClickListener { onDeletar(horario) }
    }

    override fun getItemCount(): Int = horarios.size

    fun updateList(newList: List<HorarioNutricionista>) {
        horarios = newList
        notifyDataSetChanged()
    }
} 