package com.example.aplicacionmovil.nav

sealed class Route(val path: String) {
    data object Splash : Route("splash")
    data object Login : Route("login")
    data object Register : Route("register")
    data object Home : Route("home")
    data object ForgotPassword : Route("forgot_password")
    data object ResetPassword : Route("reset_password")
    
    // CRUD Admin
    data object ListUsers : Route("list_users")
    data object CreateUser : Route("create_user")
    data object EditUser : Route("edit_user")
    
    // Datos Desarrollador
    data object Perfil : Route("perfil")
}
