package com.example.aplicacionmovil.data.remote.dto

data class ForgotPasswordRequest(val email: String)

data class ResetPasswordRequest(
    val email: String,
    val code: String,
    val newPass: String
)

data class SimpleResponse(
    val success: Boolean,
    val message: String
)
