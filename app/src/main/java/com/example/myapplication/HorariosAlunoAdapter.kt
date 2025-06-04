package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.HorarioAtendimento

class HorariosAlunoAdapter(
    private var horarios: List<HorarioAtendimento>,
    private val onAgendar: (HorarioAtendimento) -> Unit
) : RecyclerView.Adapter<HorariosAlunoAdapter.HorarioViewHolder>() {

    inner class HorarioViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtDataHora: TextView = itemView.findViewById(R.id.txt_data_hora)
        val txtTipo: TextView = itemView.findViewById(R.id.txt_tipo)
        val btnAgendar: Button = itemView.findViewById(R.id.btn_agendar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HorarioViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_horario_aluno, parent, false)
        return HorarioViewHolder(view)
    }

    override fun onBindViewHolder(holder: HorarioViewHolder, position: Int) {
        val horario = horarios[position]
        holder.txtDataHora.text = horario.data_hora
        holder.txtTipo.text = "Tipo: ${horario.tipo}"
        holder.btnAgendar.setOnClickListener { onAgendar(horario) }
    }

    override fun getItemCount(): Int = horarios.size

    fun updateList(newList: List<HorarioAtendimento>) {
        horarios = newList
        notifyDataSetChanged()
    }
} 