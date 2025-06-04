package com.example.myapplication.model

data class MedidasAluno(
    val id: String,
    val usuario_id: String,
    val peso: Double?,
    val altura: Double?,
    val idade: Int?,
    val biceps_esquerdo: Double?,
    val biceps_direito: Double?,
    val imc: Double?,
    val cintura: Double?,
    val data_registro: String?
) 