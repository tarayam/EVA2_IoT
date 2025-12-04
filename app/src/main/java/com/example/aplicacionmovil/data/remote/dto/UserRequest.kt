package com.example.aplicacionmovil.data.remote.dto

import com.squareup.moshi.Json

data class UserRequest(
    val name: String,
    @Json(name = "last_name") val lastName: String, // Mapeamos lastName de Kotlin a last_name de JSON
    val email: String,
    val password: String? = null
)
