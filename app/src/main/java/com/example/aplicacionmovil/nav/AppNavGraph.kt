package com.example.aplicacionmovil.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aplicacionmovil.R
import com.example.aplicacionmovil.screens.*
import com.example.aplicacionmovil.screens.login.AuthState
import com.example.aplicacionmovil.screens.login.AuthViewModel

@Composable
fun AppNavGraph(
    vm: AuthViewModel = viewModel()
) {
    val nav = rememberNavController()
    val authState by vm.authState.collectAsState()

    NavHost(
        navController = nav,
        startDestination = "splash"
    ) {
        composable("splash") {

            LaunchedEffect(authState) {
                when (authState) {
                    AuthState.Checking -> {
                        // sigue en el splash
                    }
                    is AuthState.Authenticated -> {
                        nav.navigate(Route.Home.path) {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                    AuthState.Unauthenticated,
                    is AuthState.Error -> {
                        nav.navigate(Route.Login.path) {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            }

            // Tu animación / imagen de splash
            SplashScreen()
        }

        composable(Route.Login.path) {
            LoginScreen(
                nav = nav,
                vm = vm
            )
        }

        composable(Route.Home.path) {
            HomeScreen(
                vm = vm,
                onLogoutDone = {
                    nav.navigate(Route.Login.path) {
                        popUpTo(Route.Home.path) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Register.path) {
            RegisterScreen(nav = nav)
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null,
            modifier = Modifier
                .size(128.dp)
                .align(Alignment.Center)
        )
    }
}
