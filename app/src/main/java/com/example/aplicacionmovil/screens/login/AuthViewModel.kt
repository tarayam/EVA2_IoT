package com.example.aplicacionmovil.screens.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionmovil.data.AuthRepository
import com.example.aplicacionmovil.data.local.TokenStorage
import com.example.aplicacionmovil.data.remote.dto.UserDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ----- ESTADOS DE AUTENTICACIÓN -----
sealed class AuthState {
    data object Checking : AuthState()              // Splash chequeando
    data object Unauthenticated : AuthState()       // Ir a Login
    data class Authenticated(val user: UserDto) : AuthState() // Ir a Home
    data class Error(val message: String) : AuthState()
}

// ----- VIEWMODEL -----
class AuthViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repo = AuthRepository()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Checking)
    val authState: StateFlow<AuthState> = _authState

    init {
        checkSession()
    }

    private fun appContext() = getApplication<Application>().applicationContext

    fun checkSession() {
        viewModelScope.launch {
            val ctx = appContext()

            val token = repo.getStoredToken(ctx)
            if (token.isNullOrEmpty()) {
                _authState.value = AuthState.Unauthenticated
                return@launch
            }

            val res = repo.validateToken(ctx)
            _authState.value = res.fold(
                onSuccess = { user -> AuthState.Authenticated(user) },
                onFailure = { AuthState.Unauthenticated }
            )
        }
    }

    fun login(email: String, pass: String) {
        _authState.value = AuthState.Checking

        viewModelScope.launch {
            val ctx = appContext()
            val res = repo.login(ctx, email, pass)

            _authState.value = res.fold(
                onSuccess = { AuthState.Authenticated(it.user) },
                onFailure = {
                    AuthState.Error(it.message ?: "Error al iniciar sesión")
                }
            )
        }
    }

    fun logout() {
        viewModelScope.launch {
            TokenStorage.clearToken(appContext())
            _authState.value = AuthState.Unauthenticated
        }
    }

    // ----- RECUPERACIÓN CONTRASEÑA -----
    fun forgotPassword(email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val res = repo.forgotPassword(email)
            res.fold(
                onSuccess = { onResult(true, it) },
                onFailure = { onResult(false, it.message ?: "Error desconocido") }
            )
        }
    }

    fun resetPassword(email: String, code: String, newPass: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val res = repo.resetPassword(email, code, newPass)
            res.fold(
                onSuccess = { onResult(true, it) },
                onFailure = { onResult(false, it.message ?: "Error desconocido") }
            )
        }
    }
}
