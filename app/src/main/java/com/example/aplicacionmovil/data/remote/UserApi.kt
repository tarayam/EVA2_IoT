package com.example.aplicacionmovil.data.remote

import com.example.aplicacionmovil.data.remote.dto.CreateUserResponse
import com.example.aplicacionmovil.data.remote.dto.ForgotPasswordResponse
import com.example.aplicacionmovil.data.remote.dto.UserDto
import com.example.aplicacionmovil.data.remote.dto.UserRequest
import retrofit2.http.*


//METODOS PARA LA GESTION DE USUARIOS

interface UserApi {

    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserDto

    @POST("auth/register") 
    suspend fun createUser(@Body user: UserRequest): CreateUserResponse // Cambiado a CreateUserResponse

    @PUT("users/{id}")
    suspend fun updateUser(@Path("id") id: Int, @Body user: UserRequest): UserDto

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): ForgotPasswordResponse
}
