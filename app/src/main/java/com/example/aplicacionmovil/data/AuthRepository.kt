package com.example.aplicacionmovil.data

import android.content.Context
import android.util.Log
import com.example.aplicacionmovil.data.local.TokenStorage
import com.example.aplicacionmovil.data.remote.AuthApi
import com.example.aplicacionmovil.data.remote.HttpClient
import com.example.aplicacionmovil.data.remote.dto.ForgotPasswordRequest
import com.example.aplicacionmovil.data.remote.dto.ResetPasswordRequest
import com.example.aplicacionmovil.data.remote.dto.LoginRequest
import com.example.aplicacionmovil.data.remote.dto.LoginResponse
import com.example.aplicacionmovil.data.remote.dto.RegisterRequest

//import com.example.aplicacionmovil.data.remote.dto.SimpleResponse
import com.example.aplicacionmovil.data.remote.dto.UserDto

class AuthRepository(
    private val api: AuthApi = HttpClient.authApi
) {

    // ---------- LOGIN ----------
    suspend fun login(ctx: Context, email: String, password: String): Result<LoginResponse> {
        return try {
            val body = LoginRequest(email = email, password = password)
            val response = api.login(body)

            if (!response.success) {
                return Result.failure(Exception("Credenciales inválidas"))
            }

            // Guardar token
            TokenStorage.saveToken(ctx, response.token)

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------- TOKEN ----------
    suspend fun getStoredToken(ctx: Context): String? {
        return TokenStorage.getToken(ctx)
    }

    suspend fun clearToken(ctx: Context) {
        TokenStorage.clearToken(ctx)
    }

    // ---------- VALIDAR TOKEN ----------
    suspend fun validateToken(ctx: Context): Result<UserDto> {
        return try {
            val token = getStoredToken(ctx)
                ?: return Result.failure(Exception("Sin token guardado"))

            val user = api.profile("Bearer $token")

            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // ---------- REGISTER ----------
    suspend fun register(name: String, lastName: String, email: String, password: String): Result<String> {
        return try {
            Log.d("AuthRepository", "Intentando registro: $name $lastName")
            val body = RegisterRequest(name, lastName, email, password)
            val response = api.register(body)

            // Log de depuración
            Log.d("AuthRepository", "Registro respuesta: message=${response.message}")

            Result.success(response.message)
        } catch (e: Exception) {
            Log.e("AuthRepository", "Error en registro", e)
            Result.failure(e)
        }
    }

    // ---------- RECUPERACIÓN PASSWORD ----------
    suspend fun forgotPassword(email: String): Result<String> {
        return try {
            val res = api.forgotPassword(ForgotPasswordRequest(email))
            Result.success(res.message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetPassword(email: String, code: String, newPass: String): Result<String> {
        return try {
            val res = api.resetPassword(ResetPasswordRequest(email = email, code = code, newPassword = newPass))
            Result.success(res.message)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
