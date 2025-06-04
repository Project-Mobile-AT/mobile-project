package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.model.Usuario

class ListaAlunosAdapter(
    private var alunos: List<Usuario>,
    private val onClick: (Usuario) -> Unit
) : RecyclerView.Adapter<ListaAlunosAdapter.AlunoViewHolder>() {

    inner class AlunoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nomeAluno: TextView = itemView.findViewById(R.id.nome_aluno)
        init {
            itemView.setOnClickListener {
                onClick(alunos[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlunoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_aluno_card, parent, false)
        return AlunoViewHolder(view)
    }

    override fun onBindViewHolder(holder: AlunoViewHolder, position: Int) {
        holder.nomeAluno.text = alunos[position].nome
    }

    override fun getItemCount(): Int = alunos.size

    fun updateList(newList: List<Usuario>) {
        alunos = newList
        notifyDataSetChanged()
    }
} 