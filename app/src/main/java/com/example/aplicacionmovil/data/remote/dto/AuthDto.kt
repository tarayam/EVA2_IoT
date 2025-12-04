package com.example.aplicacionmovil.data.remote.dto

import com.squareup.moshi.Json

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Int,
    val name: String,
    @Json(name = "last_name") val lastName: String? = "",
    val email: String
)

data class CreateUserResponse(
    val message: String,
    val user: UserDto
)
