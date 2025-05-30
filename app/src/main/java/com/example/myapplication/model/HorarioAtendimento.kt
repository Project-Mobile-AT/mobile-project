package com.example.myapplication.model

data class HorarioAtendimento(
    val id: String,
    val admin_id: String,
    val data_hora: String,
    val tipo: String, // "personal" ou "nutricionista"
    val disponivel: Boolean
)