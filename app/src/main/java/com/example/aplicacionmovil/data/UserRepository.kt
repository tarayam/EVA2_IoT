package com.example.aplicacionmovil.data

import com.example.aplicacionmovil.data.remote.HttpClient
import com.example.aplicacionmovil.data.remote.UserApi
import com.example.aplicacionmovil.data.remote.dto.UserDto
import com.example.aplicacionmovil.data.remote.dto.UserRequest

class UserRepository(
    private val api: UserApi = HttpClient.userApi
) {

    suspend fun getUsers(): Result<List<UserDto>> {
        return try {
            val users = api.getUsers()
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserById(id: Int): Result<UserDto> {
        return try {
            val user = api.getUserById(id)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createUser(user: UserRequest): Result<UserDto> {
        return try {
            val response = api.createUser(user)
            Result.success(response.user) // Devolvemos el UserDto que viene dentro de la respuesta
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUser(id: Int, user: UserRequest): Result<UserDto> {
        return try {
            val updatedUser = api.updateUser(id, user)
            Result.success(updatedUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(id: Int): Result<String> {
        return try {
            val response = api.deleteUser(id)
            Result.success(response.message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
