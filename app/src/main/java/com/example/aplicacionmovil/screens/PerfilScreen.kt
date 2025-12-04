package com.example.aplicacionmovil.screens

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aplicacionmovil.R

@Composable
fun PerfilScreen(nav: NavController) {
    val context = LocalContext.current
    // Cargar imagen desde assets para José
    val joseImageBitmap = remember {
        try {
            val inputStream = context.assets.open("Images/onizuka.png")
            BitmapFactory.decodeStream(inputStream).asImageBitmap()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()), // Permitir scroll si el contenido es largo
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Primer Perfil (Tomás)
        Image(
            painter = painterResource(R.drawable.tomas),
            contentDescription = "Foto de perfil Tomás",
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
        
        Spacer(Modifier.height(48.dp)) // Espaciador grande entre perfiles

        // Segundo Perfil (José)
        if (joseImageBitmap != null) {
            Image(
                bitmap = joseImageBitmap,
                contentDescription = "Foto de perfil José",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
            )
        } else {
            // Placeholder por si falla la carga del asset
             Text("Imagen no encontrada", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        Text(
            text = "José Álvarez",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Rol: Desarrollador Fullstack",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "Correo: jose.alvarez89@inacapmail.cl",
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = "github.com/Milosky111",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(32.dp))

        Button(onClick = { nav.popBackStack() }) {
            Text("Volver")
        }
    }
}
