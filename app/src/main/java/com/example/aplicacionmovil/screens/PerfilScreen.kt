package com.example.aplicacionmovil.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionmovil.R

@Composable
fun PerfilScreen(nav: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.tomas),
            contentDescription = "Foto de perfil",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
        )
        
        Spacer(Modifier.height(16.dp))
        
        Text(
            text = "Tomás Araya Maita",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(Modifier.height(8.dp))
        
        Text(
            text = "Rol: Desarrollador Fullstack",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Text(
            text = "Correo: taraya@inacapmail.cl",
            style = MaterialTheme.typography.bodyLarge
        )
        
        Text(
            text = "GitHub: github.com/tomasaraya",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(Modifier.height(32.dp))
        
        Button(onClick = { nav.popBackStack() }) {
            Text("Volver")
        }
    }
}
