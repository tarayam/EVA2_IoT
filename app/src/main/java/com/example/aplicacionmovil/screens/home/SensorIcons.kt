package com.example.aplicacionmovil.screens.home

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.example.aplicacionmovil.R

// 1. Lógica para obtener el ÍCONO (Drawable)
@DrawableRes
fun getTempIcon(temperature: Float?): Int {
    if (temperature == null) return R.drawable.ic_temp_media
    return when {
        temperature < 15f -> R.drawable.ic_temp_media
        temperature <= 28f -> R.drawable.ic_temp_media
        else -> R.drawable.ic_temp_media
    }
}

// 2. Lógica para obtener el COLOR de la Temperatura
fun getTempColor(temperature: Float?): Color {
    if (temperature == null) return Color.Gray
    return when {
        temperature < 15f -> Color(0xFF2196F3) // Azul (Frío)
        temperature <= 28f -> Color(0xFF4CAF50) // Verde (Ideal)
        else -> Color(0xFFFF5722)              // Rojo/Naranja (Calor)
    }
}

// 3. Lógica para obtener el ÍCONO de Humedad
@DrawableRes
fun getHumidityIcon(humidity: Float?): Int {
    if (humidity == null) return R.drawable.ic_hum_media
    return when {
        humidity < 30f -> R.drawable.ic_hum_baja
        humidity <= 60f -> R.drawable.ic_hum_media
        else -> R.drawable.ic_hum_alta
    }
}

// 4. Lógica para obtener el COLOR de la Humedad
fun getHumidityColor(humidity: Float?): Color {
    if (humidity == null) return Color.Gray
    return when {
        humidity < 30f -> Color(0xFFFFA726) // Naranja (Seco)
        humidity <= 60f -> Color(0xFF2196F3) // Azul (Normal)
        else -> Color(0xFF0D47A1)           // Azul Oscuro (Muy Húmedo)
    }
}

