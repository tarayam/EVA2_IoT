package com.example.aplicacionmovil.screens.users

import android.util.Log
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
    val filteredUsers: List<UserDto> = emptyList(),
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
            Log.d("UserViewModel", "Intentando cargar usuarios...")
            state = state.copy(isLoading = true, error = null, successMessage = null)
            val result = repository.getUsers()
            state = result.fold(
                onSuccess = { 
                    Log.d("UserViewModel", "Usuarios cargados exitosamente: ${it.size}")
                    state.copy(isLoading = false, users = it, filteredUsers = it) 
                },
                onFailure = { 
                    Log.e("UserViewModel", "Error cargando usuarios", it)
                    val msg = if (it.message?.contains("404") == true) {
                        "Error 404: Ruta no encontrada. Asegúrate de haber agregado GET /users en tu backend."
                    } else {
                        "Error: ${it.message}"
                    }
                    state.copy(isLoading = false, error = msg) 
                }
            )
        }
    }

    fun filterUsers(query: String) {
        val filtered = if (query.isBlank()) {
            state.users
        } else {
            state.users.filter { 
                it.name.contains(query, ignoreCase = true) || 
                (it.lastName?.contains(query, ignoreCase = true) == true) || // Filtramos también por apellido
                it.email.contains(query, ignoreCase = true)
            }
        }
        state = state.copy(filteredUsers = filtered)
    }

    // Ahora recibe lastName explícitamente
    fun createUser(name: String, lastName: String, email: String, pass: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            Log.d("UserViewModel", "Creando usuario: $name $lastName ($email)")
            state = state.copy(isLoading = true, error = null, successMessage = null)
            
            // Creamos el UserRequest con los campos separados
            val req = UserRequest(name, lastName, email, pass)
            val result = repository.createUser(req)
            
            state = result.fold(
                onSuccess = {
                    Log.d("UserViewModel", "Usuario creado OK")
                    loadUsers() 
                    state.copy(isLoading = false, successMessage = "Usuario creado correctamente")
                },
                onFailure = { 
                    Log.e("UserViewModel", "Error creando usuario", it)
                    state.copy(isLoading = false, error = it.message) 
                }
            )
            onResult(result.isSuccess)
        }
    }

    // Para update, también deberíamos separar si el backend lo soporta, pero lo dejaré compatible
    // Asumiendo que update también espera last_name
    fun updateUser(id: Int, name: String, lastName: String, email: String, pass: String?, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            Log.d("UserViewModel", "Actualizando usuario ID: $id")
            state = state.copy(isLoading = true, error = null, successMessage = null)
            
            val req = UserRequest(name, lastName, email, pass.takeIf { !it.isNullOrBlank() })
            val result = repository.updateUser(id, req)

            state = result.fold(
                onSuccess = {
                    Log.d("UserViewModel", "Usuario actualizado OK")
                    loadUsers()
                    state.copy(isLoading = false, successMessage = "Usuario actualizado")
                },
                onFailure = { 
                    Log.e("UserViewModel", "Error actualizando usuario", it)
                    state.copy(isLoading = false, error = it.message) 
                }
            )
            onResult(result.isSuccess)
        }
    }

    fun deleteUser(id: Int) {
        viewModelScope.launch {
            Log.d("UserViewModel", "Eliminando usuario ID: $id")
            state = state.copy(isLoading = true, error = null, successMessage = null)
            val result = repository.deleteUser(id)
            
            state = result.fold(
                onSuccess = {
                    Log.d("UserViewModel", "Usuario eliminado OK")
                    loadUsers()
                    state.copy(isLoading = false, successMessage = "Usuario eliminado")
                },
                onFailure = { 
                    Log.e("UserViewModel", "Error eliminando usuario", it)
                    state.copy(isLoading = false, error = it.message) 
                }
            )
        }
    }

    fun clearMessages() {
        state = state.copy(error = null, successMessage = null)
    }
}
