package com.example.aplicacionmovil.nav

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.aplicacionmovil.screens.*
import com.example.aplicacionmovil.screens.login.AuthState
import com.example.aplicacionmovil.screens.login.AuthViewModel
import com.example.aplicacionmovil.screens.users.ListarUsuariosScreen
import com.example.aplicacionmovil.screens.users.ModificarUsuarioScreen
import com.example.aplicacionmovil.screens.users.RegistrarUsuarioScreen
import com.example.aplicacionmovil.screens.users.UserViewModel

@Composable
fun AppNavGraph(
    vm: AuthViewModel = viewModel(),
    userVm: UserViewModel = viewModel()
) {
    val nav = rememberNavController()
    val authState by vm.authState.collectAsState()

    NavHost(
        navController = nav,
        startDestination = Route.Splash.path
    ) {
        // 1. SPLASH
        composable(Route.Splash.path) {
            SplashScreen(navController = nav)
            
            // Verificación de sesión

        }

         //2. AUTH (LOGIN, REGISTER, RECOVERY)
        composable(Route.Login.path) {
            LoginScreen(nav = nav, vm = vm)
        }

        composable(Route.Register.path) {
            RegisterScreen(nav = nav)
        }
        
        composable(Route.ForgotPassword.path) {
            ForgotPasswordScreen(nav, vm)
        }

        composable(
            route = "${Route.ResetPassword.path}/{email}",
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            ResetPasswordScreen(nav, email, vm)
        }

        // 3. HOME (Incluye Sensores)
        composable(Route.Home.path) {
            HomeScreen(
                vm = vm,
                onLogoutDone = {
                    nav.navigate(Route.Login.path) {
                        popUpTo(Route.Home.path) { inclusive = true }
                    }
                },
                onAdminClick = { nav.navigate(Route.ListUsers.path) },
                onPerfilClick = { nav.navigate(Route.Perfil.path) }
            )
        }

        // 4. USUARIOS (CRUD)
        composable(Route.ListUsers.path) {
            ListarUsuariosScreen(nav, userVm)
        }
        
        composable(Route.CreateUser.path) {
            RegistrarUsuarioScreen(nav, userVm)
        }
        
        composable(
            route = "${Route.EditUser.path}/{id}?name={name}&email={email}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("name") { defaultValue = "" },
                navArgument("email") { defaultValue = "" }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            val name = backStackEntry.arguments?.getString("name") ?: ""
            val email = backStackEntry.arguments?.getString("email") ?: ""
            
            ModificarUsuarioScreen(nav, userVm, id, name, email)
        }

        // 5. PERFIL
        composable(Route.Perfil.path) {
            PerfilScreen(nav)
        }
    }
}
