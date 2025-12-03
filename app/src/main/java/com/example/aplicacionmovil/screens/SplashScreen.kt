package com.example.aplicacionmovil.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue // Importante para usar 'by'
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {

    // Lógica de navegación (Temporizador)
    LaunchedEffect(Unit) {
        delay(5000)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    // Carga de la animación Lottie
    // Usamos 'by' para obtener el valor directamente y eliminar el .value después
    val composition by rememberLottieComposition(
        LottieCompositionSpec.Asset("GalaxyCar_Loading.json")
    )

    // Renderizado
    LottieAnimation(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        modifier = Modifier.fillMaxSize()
    )
}
