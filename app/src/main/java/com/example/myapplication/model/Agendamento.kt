package com.example.myapplication.model

data class Agendamento(
    val id: String,
    val aluno_id: String,
    val admin_id: String?,
    val horario_id: String?,
    val aula_id: String?,
    val tipo: String, // "personal", "nutricionista" ou "aula"
    val confirmado: Boolean
)