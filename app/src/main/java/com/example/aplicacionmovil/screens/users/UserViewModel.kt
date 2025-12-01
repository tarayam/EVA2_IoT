package com.example.aplicacionmovil.screens.users

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.aplicacionmovil.data.UserRepository
import com.example.aplicacionmovil.data.remote.dto.UserDto
import com.example.aplicacionmovil.data.remote.dto.UserRequest
import kotlinx.coroutines.launch

data class UserState(
    val users: List<UserDto> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

class UserViewModel(
    private val repository: UserRepository = UserRepository()
) : ViewModel() {

    var state by mutableStateOf(UserState())
        private set

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, successMessage = null)
            val result = repository.getUsers()
            state = result.fold(
                onSuccess = { state.copy(isLoading = false, users = it) },
                onFailure = { state.copy(isLoading = false, error = it.message) }
            )
        }
    }

    fun createUser(name: String, email: String, pass: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, successMessage = null)
            val req = UserRequest(name, email, pass)
            val result = repository.createUser(req)
            
            state = result.fold(
                onSuccess = {
                    loadUsers() // Recargar lista
                    state.copy(isLoading = false, successMessage = "Usuario creado correctamente")
                },
                onFailure = { state.copy(isLoading = false, error = it.message) }
            )
            onResult(result.isSuccess)
        }
    }

    fun updateUser(id: Int, name: String, email: String, pass: String?, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, successMessage = null)
            // Si pass es vacío, enviamos null (depende de tu backend si admite null para no cambiarlo)
            // Asumimos que UserRequest soporta pass opcional
            val req = UserRequest(name, email, pass ?: "") 
            val result = repository.updateUser(id, req)

            state = result.fold(
                onSuccess = {
                    loadUsers()
                    state.copy(isLoading = false, successMessage = "Usuario actualizado")
                },
                onFailure = { state.copy(isLoading = false, error = it.message) }
            )
            onResult(result.isSuccess)
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            state = state.copy(isLoading = true, error = null, successMessage = null)
            val result = repository.deleteUser(id)
            
            state = result.fold(
                onSuccess = {
                    loadUsers()
                    state.copy(isLoading = false, successMessage = "Usuario eliminado")
                },
                onFailure = { state.copy(isLoading = false, error = it.message) }
            )
        }
    }

    fun clearMessages() {
        state = state.copy(error = null, successMessage = null)
    }
}
