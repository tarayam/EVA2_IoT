package com.example.aplicacionmovil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.aplicacionmovil.nav.AppNavGraph
import com.example.aplicacionmovil.ui.theme.AplicacionMovilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            AplicacionMovilTheme {
                AppNavGraph()
            }
        }
    }
}
