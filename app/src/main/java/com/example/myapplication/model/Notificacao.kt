package com.example.myapplication.model

data class Notificacao(
    val id: String,
    val usuario_id: String,
    val titulo: String?,
    val conteudo: String?,
    val criado_em: String?
)