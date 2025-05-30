package com.example.myapplication.model

data class RelacaoAdminAluno(
    val id: String,
    val admin_id: String,
    val aluno_id: String,
    val tipo: String // "personal" ou "nutricionista"
)