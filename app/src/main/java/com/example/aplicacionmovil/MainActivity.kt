package com.example.aplicacionmovil

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.aplicacionmovil.nav.AppNavGraph
import com.example.aplicacionmovil.screens.login.AuthState
import com.example.aplicacionmovil.screens.login.AuthViewModel
import com.example.aplicacionmovil.ui.theme.AplicacionMovilTheme

class MainActivity : ComponentActivity() {
    
    // Instanciamos el ViewModel aquí para poder acceder a él desde onUserInteraction
    private val authViewModel: AuthViewModel by viewModels()
    
    // Handler para el temporizador de inactividad
    private val inactivityHandler = Handler(Looper.getMainLooper())
    private val inactivityRunnable = Runnable {
        // Acción a ejecutar cuando expira el tiempo (cerrar sesión)
        logoutIfAuthenticated()
    }
    
    // Tiempo de inactividad en milisegundos (5 minutos = 300,000 ms)
    private val INACTIVITY_TIMEOUT = 5 * 60 * 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        
        // Iniciar temporizador
        resetInactivityTimer()
        
        setContent {
            AplicacionMovilTheme {
                // Añadimos Surface para que tome el color de fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Pasamos el viewModel existente al NavGraph
                    AppNavGraph(vm = authViewModel)
                }
            }
        }
    }

    // Detecta cualquier toque en la pantalla
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        resetInactivityTimer()
        return super.dispatchTouchEvent(ev)
    }

    // Reinicia el contador de inactividad
    private fun resetInactivityTimer() {
        inactivityHandler.removeCallbacks(inactivityRunnable)
        inactivityHandler.postDelayed(inactivityRunnable, INACTIVITY_TIMEOUT)
    }

    private fun logoutIfAuthenticated() {
        // Solo cerrar sesión si el usuario está autenticado actualmente
        if (authViewModel.authState.value is AuthState.Authenticated) {
            authViewModel.logout()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Limpiar el handler para evitar fugas de memoria
        inactivityHandler.removeCallbacks(inactivityRunnable)
    }
}
