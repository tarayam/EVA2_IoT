package com.example.aplicacionmovil.screens.users

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionmovil.nav.Route

@Composable
fun ListarUsuariosScreen(
    nav: NavController,
    vm: UserViewModel
) {
    val state = vm.state

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { nav.navigate(Route.CreateUser.path) }) {
                Icon(Icons.Default.Add, contentDescription = "Crear Usuario")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Gestión de Usuarios", style = MaterialTheme.typography.headlineMedium)
            
            Spacer(Modifier.height(16.dp))

            // Mensajes
            state.error?.let {
                Text(it, color = Color.Red)
                Spacer(Modifier.height(8.dp))
            }
            state.successMessage?.let {
                Text(it, color = Color(0xFF006400)) // Verde oscuro
                Spacer(Modifier.height(8.dp))
            }

            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            LazyColumn {
                items(state.users) { user ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(user.name, style = MaterialTheme.typography.titleMedium)
                                Text(user.email, style = MaterialTheme.typography.bodyMedium)
                            }
                            Row {
                                IconButton(onClick = { 
                                    // Navegar a edición pasando params (simplificado)
                                    nav.navigate("${Route.EditUser.path}/${user.id}?name=${user.name}&email=${user.email}")
                                }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color.Blue)
                                }
                                IconButton(onClick = { vm.deleteUser(user.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
