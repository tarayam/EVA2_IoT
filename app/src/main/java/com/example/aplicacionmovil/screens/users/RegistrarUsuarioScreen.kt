package com.example.aplicacionmovil.screens.users

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun RegistrarUsuarioScreen(
    nav: NavController,
    vm: UserViewModel
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var pass by remember { mutableStateOf("") }
    
    // Estados para manejar diálogos de error
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    // Estado para marcar el campo de email con error visualmente
    var isEmailError by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val state = vm.state

    // Limpiamos mensajes al entrar
    LaunchedEffect(Unit) { vm.clearMessages() }
    
    // Observar cambios en el estado para mostrar errores que vengan del ViewModel
    LaunchedEffect(state.error) {
        state.error?.let {
            if (it.contains("409") || it.contains("Conflict")) {
                errorMessage = "El correo ya se encuentra en uso."
                isEmailError = true
            } else {
                errorMessage = it
            }
            showErrorDialog = true
            vm.clearMessages() 
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registrar Usuario", fontSize = 24.sp)
        Spacer(Modifier.height(24.dp))
        
        OutlinedTextField(
            value = name, 
            onValueChange = { name = it }, 
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        
        OutlinedTextField(
            value = lastName, 
            onValueChange = { lastName = it }, 
            label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = email, 
            onValueChange = { 
                email = it 
                isEmailError = false // Limpiar error al escribir
            }, 
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            isError = isEmailError
        )
        // Texto de ayuda si hay error en el email
        if (isEmailError) {
            Text(
                text = "Este correo ya está registrado",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(Modifier.height(12.dp))
        
        OutlinedTextField(
            value = pass, 
            onValueChange = { pass = it }, 
            label = { Text("Contraseña") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = PasswordVisualTransformation()
        )
        
        Spacer(Modifier.height(24.dp))
        
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    // Validaciones locales
                    val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    val hasUpperCase = pass.any { it.isUpperCase() }
                    val hasDigit = pass.any { it.isDigit() }
                    val isLengthValid = pass.length >= 6

                    if (name.isBlank() || lastName.isBlank()) {
                        errorMessage = "Nombre y Apellido son obligatorios."
                        showErrorDialog = true
                    } else if (!isEmailValid) {
                        errorMessage = "El formato del correo electrónico no es válido."
                        showErrorDialog = true
                        isEmailError = true
                    } else if (!isLengthValid || !hasUpperCase || !hasDigit) {
                        errorMessage = "La contraseña debe tener al menos 6 caracteres, incluir una mayúscula y un número."
                        showErrorDialog = true
                    } else {
                        // Llamamos al ViewModel
                        vm.createUser(name, lastName, email, pass) { success ->
                            if (success) {
                                Toast.makeText(context, "Usuario creado exitosamente", Toast.LENGTH_SHORT).show()
                                nav.popBackStack()
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar")
            }
        }
        
        // Diálogo de Error
        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text("Error") },
                text = { Text(errorMessage) },
                confirmButton = {
                    TextButton(onClick = { showErrorDialog = false }) {
                        Text("Entendido")
                    }
                }
            )
        }
    }
}
