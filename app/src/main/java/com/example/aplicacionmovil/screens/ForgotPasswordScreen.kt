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
fun ForgotPasswordScreen(
    nav: NavController,
    vm: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Recuperar contraseña", fontSize = 24.sp)
        
        Spacer(Modifier.height(20.dp))
        
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Ingresa tu Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(20.dp))
        
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        isLoading = true
                        msg = null
                        vm.forgotPassword(email) { success, message ->
                            isLoading = false
                            isError = !success
                            msg = message
                            if (success) {
                                // Pasamos el email a la siguiente pantalla
                                nav.navigate("${Route.ResetPassword.path}/$email")
                            }
                        }
                    } else {
                        isError = true
                        msg = "Email inválido"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enviar código")
            }
        }
        
        msg?.let {
            Spacer(Modifier.height(12.dp))
            Text(it, color = if (isError) Color.Red else Color.Blue)
        }
        
        Spacer(Modifier.height(12.dp))
        TextButton(onClick = { nav.popBackStack() }) {
            Text("Volver")
        }
    }
}
