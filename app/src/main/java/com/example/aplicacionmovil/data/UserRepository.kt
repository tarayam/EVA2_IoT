package com.example.aplicacionmovil.data

import com.example.aplicacionmovil.data.remote.HttpClient
import com.example.aplicacionmovil.data.remote.UserApi
import com.example.aplicacionmovil.data.remote.dto.SimpleResponse
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
            val newUser = api.createUser(user)
            Result.success(newUser)
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

    suspend fun deleteUser(id: Int): Result<SimpleResponse> {
        return try {
            val response = api.deleteUser(id)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
