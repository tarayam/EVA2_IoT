package com.example.aplicacionmovil.screens.users

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ModificarUsuarioScreen(
    nav: NavController,
    vm: UserViewModel,
    userId: Int,
    currentName: String,
    currentEmail: String
) {
    // Intentamos separar nombre y apellido si vienen juntos en 'currentName'
    // OJO: Si usas navegación pasando argumentos, quizás debas pasar 'currentLastName' también
    // Por ahora, asumimos que 'currentName' podría traer todo junto y lo separamos simple.
    val parts = currentName.split(" ")
    val initName = if (parts.isNotEmpty()) parts[0] else ""
    val initLastName = if (parts.size > 1) parts.drop(1).joinToString(" ") else ""

    var name by remember { mutableStateOf(initName) }
    var lastName by remember { mutableStateOf(initLastName) }
    var email by remember { mutableStateOf(currentEmail) }
    var pass by remember { mutableStateOf("") } // Contraseña opcional
    
    LaunchedEffect(Unit) { vm.clearMessages() }

    val state = vm.state

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Editar Usuario #$userId", fontSize = 24.sp)
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
            onValueChange = { email = it }, 
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = pass, 
            onValueChange = { pass = it }, 
            label = { Text("Nueva Contraseña (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(24.dp))
        
        if (state.isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    val newPass = pass.ifBlank { null }
                    // Ahora llamamos a updateUser con lastName
                    vm.updateUser(userId, name, lastName, email, newPass) { success ->
                        if (success) nav.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Actualizar")
            }
        }
        
        state.error?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = Color.Red)
        }
    }
}
