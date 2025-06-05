package com.example.myapplication.model

data class HorarioAtendimento(
    val id: String,
    val admin_id: String,
    val data_hora: String,
    val tipo: String, // "personal" ou "nutricionista"
    val disponivel: Boolean,
    val aluno_id: String? = null,
    val aluno_nome: String? = null,
    val horario_inicio: String? = null,
    val horario_fim: String? = null,
    val dia_da_semana: String? = null
)