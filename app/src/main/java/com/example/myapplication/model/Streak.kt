package com.example.myapplication.model

data class Streak(
    val id: String,
    val usuario_id: String,
    val data: String,
    val completado: Boolean
)