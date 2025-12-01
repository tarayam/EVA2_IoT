package com.example.aplicacionmovil.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplicacionmovil.nav.Route
import com.example.aplicacionmovil.screens.login.AuthViewModel

@Composable
fun ResetPasswordScreen(
    nav: NavController,
    email: String,
    vm: AuthViewModel = viewModel()
) {
    var code by remember { mutableStateOf("") }
    var newPass by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Restablecer contraseña", fontSize = 24.sp)
        Text("Email: $email", fontSize = 14.sp, color = Color.Gray)
        
        Spacer(Modifier.height(20.dp))
        
        OutlinedTextField(
            value = code,
            onValueChange = { 
                if (it.length <= 5) code = it 
            },
            label = { Text("Código (5 dígitos)") },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(Modifier.height(12.dp))
        
        OutlinedTextField(
            value = newPass,
            onValueChange = { newPass = it },
            label = { Text("Nueva contraseña") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))
        
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    // Validaciones locales básicas
                    if (code.length != 5) {
                        isError = true
                        msg = "El código debe tener 5 dígitos"
                        return@Button
                    }
                    if (newPass.length < 6) {
                        isError = true
                        msg = "Contraseña débil (mínimo 6 caracteres)"
                        return@Button
                    }
                    
                    isLoading = true
                    msg = null
                    vm.resetPassword(email, code, newPass) { success, message ->
                        isLoading = false
                        isError = !success
                        msg = message
                        if (success) {
                            // Éxito: ir a Login
                            // Podríamos poner un delay para que el usuario lea "Contraseña cambiada"
                            nav.navigate(Route.Login.path) {
                                popUpTo(Route.Login.path) { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cambiar contraseña")
            }
        }
        
        msg?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = if (isError) Color.Red else Color.Blue)
        }
    }
}
