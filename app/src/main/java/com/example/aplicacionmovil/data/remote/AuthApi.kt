package com.example.aplicacionmovil.data.remote

import com.example.aplicacionmovil.data.remote.dto.ForgotPasswordRequest
import com.example.aplicacionmovil.data.remote.dto.LoginRequest
import com.example.aplicacionmovil.data.remote.dto.LoginResponse
import com.example.aplicacionmovil.data.remote.dto.ResetPasswordRequest
import com.example.aplicacionmovil.data.remote.dto.SimpleResponse
import com.example.aplicacionmovil.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @GET("profile")
    suspend fun profile(
        @Header("Authorization") auth: String
    ): UserDto

    // Recuperación de contraseña
    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): SimpleResponse

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): SimpleResponse
}
