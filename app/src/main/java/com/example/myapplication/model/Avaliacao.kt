package com.example.myapplication.model

data class Avaliacao(
    val id: String,
    val usuario_id: String,
    val data: String?,
    val peso: Double?,
    val massa_muscular: Double?,
    val gordura: Double?,
    val agua: Double?,
    val medicoes: String?
)