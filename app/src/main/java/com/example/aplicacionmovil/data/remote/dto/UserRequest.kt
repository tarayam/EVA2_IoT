package com.example.aplicacionmovil.data.remote.dto

data class UserRequest(
    val name: String,
    val email: String,
    val password: String? = null // Opcional en edición
)
