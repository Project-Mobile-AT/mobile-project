package com.example.myapplication.model

data class Aula(
    val id: String,
    val horario_id: String,
    val titulo: String,
    val descricao: String?,
    val instrutor: String?,
    val status: String
)

data class AulaPost(
    val horario_id: String,
    val titulo: String,
    val descricao: String,
    val instrutor: String,
    val status: String
)

data class AulaPatch(
    val horario_id: String,
    val titulo: String,
    val descricao: String?,
    val instrutor: String?,
    val status: String
)