package com.example.aplicacionmovil.data.remote


import com.example.aplicacionmovil.data.remote.dto.LoginRequest
import com.example.aplicacionmovil.data.remote.dto.LoginResponse

import com.example.aplicacionmovil.data.remote.dto.RegisterRequest
import com.example.aplicacionmovil.data.remote.dto.RegisterResponse
import com.example.aplicacionmovil.data.remote.dto.ForgotPasswordRequest
import com.example.aplicacionmovil.data.remote.dto.ForgotPasswordResponse
import com.example.aplicacionmovil.data.remote.dto.ResetPasswordRequest
import com.example.aplicacionmovil.data.remote.dto.ResetPasswordResponse
import com.example.aplicacionmovil.data.remote.dto.UserDto

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.DELETE

interface AuthApi {

    //LOGIN

    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse
    @GET("profile")
    suspend fun profile(
        @Header("Authorization") auth: String
    ): UserDto

    //REGISTRO

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): RegisterResponse

    //PERDIDA Y RECUPERACION DE CONTRASEÑAS

    @POST("auth/forgot-password")
    suspend fun forgotPassword(@Body body: ForgotPasswordRequest): ForgotPasswordResponse

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body body: ResetPasswordRequest): ResetPasswordResponse
    
    // CRUD PARA GESTION DE USUARIOS
    /*
    @GET("admin/users")
    suspend fun getUsers(): UserListResponse  // Devuelve una lista de usuarios

    @POST("admin/users")
    suspend fun createUser(@Body body: RegisterRequest): UserActionResponse

    @PUT("admin/users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body body: UserDto): UserActionResponse

    @DELETE("admin/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): UserActionResponse
    */
}