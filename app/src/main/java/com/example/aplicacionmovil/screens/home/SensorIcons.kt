package com.example.aplicacionmovil.screens.home

import androidx.annotation.DrawableRes
import com.example.aplicacionmovil.R

@DrawableRes
fun getTempIcon(temperature: Float?): Int {
    if (temperature == null) return R.drawable.ic_temp_media
    return when {
        temperature < 15f -> R.drawable.ic_temp_baja
        temperature <= 28f -> R.drawable.ic_temp_media
        else -> R.drawable.ic_temp_alta
    }
}

@DrawableRes
fun getHumidityIcon(humidity: Float?): Int {
    if (humidity == null) return R.drawable.ic_hum_media
    return when {
        humidity < 30f -> R.drawable.ic_hum_baja
        humidity <= 60f -> R.drawable.ic_hum_media
        else -> R.drawable.ic_hum_alta
    }
}
