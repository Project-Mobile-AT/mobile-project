package com.example.myapplication.model

data class HorarioNutricionista(
    val id: String,
    val admin_id: String,
    val data_hora: String,
    val disponivel: Boolean,
    val aluno_id: String? = null,
    val aluno_nome: String? = null,
    val criado_em: String? = null
) 