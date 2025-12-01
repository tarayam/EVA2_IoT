package com.example.aplicacionmovil.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacionmovil.data.remote.dto.UserDto
import com.example.aplicacionmovil.screens.home.SensorUiState
import com.example.aplicacionmovil.screens.home.SensorViewModel
import com.example.aplicacionmovil.screens.home.getHumidityIcon
import com.example.aplicacionmovil.screens.home.getTempIcon
import com.example.aplicacionmovil.screens.login.AuthState
import com.example.aplicacionmovil.screens.login.AuthViewModel
import com.example.aplicacionmovil.ui.theme.AplicacionMovilTheme

@Composable
fun HomeContent(
    authState: AuthState,
    sensorState: SensorUiState,
    onLogout: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // 1) Mensaje de bienvenida
        if (authState is AuthState.Authenticated) {
            val user = authState.user
            Text(
                text = "Bienvenido ${user.name}",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(20.dp))
        } else {
            Text(
                text = "Bienvenido",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(Modifier.height(20.dp))
        }

        // 2) Estado de sensores
        if (sensorState.isLoading && sensorState.temperature == null) {
            Text("Cargando datos de sensores...")
            Spacer(Modifier.height(12.dp))
        }

        sensorState.errorMessage?.let { msg ->
            Text(
                text = msg,
                color = Color.Red,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(Modifier.height(12.dp))
        }

        // 3) Temperatura
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = getTempIcon(sensorState.temperature)),
                contentDescription = "Icono temperatura",
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(text = "Temperatura")
                Text(
                    text = sensorState.temperature?.let { "$it °C" } ?: "-- °C",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        // 4) Humedad
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = getHumidityIcon(sensorState.humidity)),
                contentDescription = "Icono humedad",
                modifier = Modifier.size(64.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(text = "Humedad")
                Text(
                    text = sensorState.humidity?.let { "$it %" } ?: "-- %",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }

        // 5) Última actualización
        sensorState.lastUpdate?.let { last ->
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Última actualización: $last",
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(Modifier.height(32.dp))

        // 6) Botón de logout
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
fun HomeScreen(
    vm: AuthViewModel = viewModel(),
    sensorVm: SensorViewModel = viewModel(),
    onLogoutDone: () -> Unit
) {
    val authState by vm.authState.collectAsState()
    val sensorState = sensorVm.uiState.value

    // Cuando se hace logout, volvemos al login
    LaunchedEffect(authState) {
        if (authState is AuthState.Unauthenticated) {
            onLogoutDone()
        }
    }

    HomeContent(
        authState = authState,
        sensorState = sensorState,
        onLogout = { vm.logout() }
    )
}

// --- PREVIEW de ejemplo ---
@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    AplicacionMovilTheme {
        HomeContent(
            authState = AuthState.Authenticated(
                user = UserDto(
                    id = 1,
                    name = "Usuario Demo",
                    email = "demo@example.com"
                )
            ),
            sensorState = SensorUiState(
                isLoading = false,
                temperature = 24.5f,
                humidity = 60f,
                lastUpdate = "2025-11-22 18:30",
                errorMessage = null
            ),
            onLogout = {}
        )
    }
}
