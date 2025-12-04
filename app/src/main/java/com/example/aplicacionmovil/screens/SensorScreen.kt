package com.example.aplicacionmovil.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.testing.TestNavHostController
import com.example.aplicacionmovil.screens.home.SensorUiState
import com.example.aplicacionmovil.screens.home.SensorViewModel
import com.example.aplicacionmovil.screens.home.getHumidityIcon
import com.example.aplicacionmovil.screens.home.getTempIcon
import com.example.aplicacionmovil.ui.theme.AplicacionMovilTheme
import androidx.compose.ui.platform.LocalContext
import com.example.aplicacionmovil.screens.home.getHumidityColor
import com.example.aplicacionmovil.screens.home.getTempColor

@Composable
fun SensorScreen(nav: NavController, vm: SensorViewModel = viewModel()) {
    val sensorState = vm.uiState.value
    // Usamos las dos funciones nuevas
    val iconResH = getTempIcon(sensorState.temperature)
    val iconColorH = getTempColor(sensorState.temperature)
    // Usamos las dos funciones nuevas
    val iconRes = getHumidityIcon(sensorState.humidity)
    val iconColor = getHumidityColor(sensorState.humidity)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Estado de Sensores",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(Modifier.height(32.dp))

        // 3) Estado de sensores
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

        // Contenedor para alinear las filas de temperatura y humedad
        Column(
            modifier = Modifier.width(250.dp), // Establecer un ancho fijo o máximo para alinear
            horizontalAlignment = Alignment.Start // Alinear contenido al inicio (izquierda)
        ) {
            // 4) Temperatura
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = iconResH),
                    contentDescription = "Icono temperatura",
                    modifier = Modifier.size(64.dp),
                    colorFilter = ColorFilter.tint(iconColorH) // ¡Aquí se aplica el color!
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

            // 5) Humedad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {

                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "Icono humedad",
                    modifier = Modifier.size(64.dp),
                    colorFilter = ColorFilter.tint(iconColor) // ¡Aquí se aplica el color!
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
        }

        // 6) Última actualización
        sensorState.lastUpdate?.let { last ->
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Última actualización: $last",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SensorScreenPreview() {
    AplicacionMovilTheme {
        SensorScreen(nav = TestNavHostController(LocalContext.current))
    }
}
