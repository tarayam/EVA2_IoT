package com.example.aplicacionmovil.screens

import android.content.Context
import android.hardware.camera2.CameraManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aplicacionmovil.R
import com.example.aplicacionmovil.data.remote.dto.UserDto
import com.example.aplicacionmovil.screens.home.SensorUiState
import com.example.aplicacionmovil.screens.home.SensorViewModel
import com.example.aplicacionmovil.screens.home.getHumidityIcon
import com.example.aplicacionmovil.screens.home.getTempIcon
import com.example.aplicacionmovil.screens.login.AuthState
import com.example.aplicacionmovil.screens.login.AuthViewModel
import com.example.aplicacionmovil.ui.theme.AplicacionMovilTheme

// Función auxiliar para encender/apagar linterna
private fun toggleTorch(context: Context, isOn: Boolean) {
    try {
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = cameraManager.cameraIdList[0] // Usualmente la cámara trasera es 0
        cameraManager.setTorchMode(cameraId, isOn)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun HomeContent(
    authState: AuthState,
    sensorState: SensorUiState,
    onLogout: () -> Unit,
    onAdminClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onSensorClick: () -> Unit
) {
    val context = LocalContext.current
    val isBulbOn = remember { mutableStateOf(false) }

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
        
        // Botón Perfil Desarrollador
        Button(
            onClick = onPerfilClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
        ) {
            Text("Ver Perfil Desarrollador")
        }
        
        Spacer(Modifier.height(20.dp))

        // 2) Ampolleta / Linterna
        Image(
            painter = painterResource(if (isBulbOn.value) R.drawable.bulb_on else R.drawable.bulb_off),
            contentDescription = null,
            modifier = Modifier
                .size(80.dp)
                .clickable {
                    isBulbOn.value = !isBulbOn.value
                    toggleTorch(context, isBulbOn.value)
                }
        )
        Text(text = if (isBulbOn.value) "Ampolleta encendida" else "Ampolleta apagada")
        
        Spacer(Modifier.height(20.dp))

        /*
        // 3) Estado de sensores (REMOVIDO DE HOME)
        // ...
        */

        Spacer(Modifier.height(12.dp))

        // 7) Botón Admin (Gestión de Usuarios)
        Button(
            onClick = onAdminClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Gestión de Usuarios")
        }

        Spacer(Modifier.height(12.dp))
        
        // NUEVO: Botón Sensores
        Button(
            onClick = onSensorClick,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Text("Ver Sensores")
        }

        Spacer(Modifier.height(12.dp))

        // 8) Botón de logout
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
    onLogoutDone: () -> Unit,
    onAdminClick: () -> Unit,
    onPerfilClick: () -> Unit,
    onSensorClick: () -> Unit
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
        onLogout = { vm.logout() },
        onAdminClick = onAdminClick,
        onPerfilClick = onPerfilClick,
        onSensorClick = onSensorClick
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
            onLogout = {},
            onAdminClick = {},
            onPerfilClick = {},
            onSensorClick = {}
        )
    }
}
