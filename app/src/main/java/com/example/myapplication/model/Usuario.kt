package com.example.myapplication.model

data class Usuario(
    val id: String,
    val email: String,
    val senha: String?,
    val is_admin: Boolean,
    val nome: String,
    val cpf: String,
    val data_nascimento: String?,
    val peso: Double?,
    val altura: Double?,
    val criado_em: String?,
    val img_perfil: String?
)