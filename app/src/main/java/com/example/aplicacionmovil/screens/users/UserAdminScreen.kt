package com.example.aplicacionmovil.screens.users

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.aplicacionmovil.data.remote.dto.UserDto
import com.example.aplicacionmovil.screens.users.UserViewModel

enum class UserScreenState {
    MENU,
    LIST,
    CREATE_FORM
}

fun isPasswordRobust(password: String): Boolean {
    return password.length >= 8 &&
            password.any { it.isDigit() } &&
            password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { !it.isLetterOrDigit() }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserManagementScreen(nav: NavController, vm: UserViewModel = viewModel(), currentUserId: Int? = null) {
    var currentScreen by remember { mutableStateOf(UserScreenState.MENU) }
    val state = vm.state
    val context = LocalContext.current

    LaunchedEffect(currentScreen) {
        if (currentScreen == UserScreenState.LIST) {
            vm.loadUsers()
        }
    }

    LaunchedEffect(state.successMessage, state.error) {
        state.successMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            if (currentScreen == UserScreenState.CREATE_FORM) {
                vm.clearMessages() 
                currentScreen = UserScreenState.LIST 
            }
        }
        state.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    BackHandler(enabled = currentScreen != UserScreenState.MENU) {
        vm.clearMessages() 
        currentScreen = UserScreenState.MENU
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        when(currentScreen) {
                            UserScreenState.MENU -> "Gestión de Usuarios"
                            UserScreenState.LIST -> "Listado de Usuarios"
                            UserScreenState.CREATE_FORM -> "Ingresar Usuario"
                        },
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                navigationIcon = {
                    if (currentScreen != UserScreenState.MENU) {
                        IconButton(onClick = { 
                            vm.clearMessages()
                            currentScreen = UserScreenState.MENU 
                        }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    } else {
                        IconButton(onClick = { nav.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                },
                actions = {
                    if (currentScreen == UserScreenState.LIST) {
                        IconButton(onClick = { vm.loadUsers() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Recargar lista", tint = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().background(MaterialTheme.colorScheme.background)) {
            when (currentScreen) {
                UserScreenState.MENU -> {
                    UserManagementMenu(
                        onGoToCreate = { 
                            vm.clearMessages()
                            currentScreen = UserScreenState.CREATE_FORM 
                        },
                        onGoToList = { 
                            vm.clearMessages()
                            currentScreen = UserScreenState.LIST 
                        }
                    )
                }
                UserScreenState.LIST -> {
                    UserManagementListContent(vm = vm, currentUserId = currentUserId)
                }
                UserScreenState.CREATE_FORM -> {
                    UserManagementCreateContent(
                        serverError = state.error,
                        isLoading = state.isLoading,
                        onConfirm = { name, lastName, email, pass ->
                            // Ahora pasamos name y lastName por separado
                            vm.createUser(name, lastName, email, pass) { }
                        },
                        onCancel = { 
                            vm.clearMessages()
                            currentScreen = UserScreenState.MENU 
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UserManagementMenu(onGoToCreate: () -> Unit, onGoToList: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MenuButton(
            text = "Ingresar Usuario",
            icon = Icons.Default.Add,
            onClick = onGoToCreate
        )
        Spacer(modifier = Modifier.height(24.dp))
        MenuButton(
            text = "Listar Usuarios",
            icon = Icons.Default.List,
            onClick = onGoToList
        )
    }
}

@Composable
fun MenuButton(text: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = MaterialTheme.shapes.medium,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.onPrimary)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun UserManagementListContent(vm: UserViewModel, currentUserId: Int?) {
    val state = vm.state
    var searchQuery by remember { mutableStateOf("") }
    
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedUser by remember { mutableStateOf<UserDto?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                vm.filterUsers(it)
            },
            label = { Text("Buscar por nombre, apellido o email") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        if (state.successMessage != null) {
            Text(
                text = state.successMessage,
                color = Color(0xFF008000), 
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        if (state.error != null) {
             Text(
                text = state.error,
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
        
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(state.filteredUsers) { user ->
                    val isCurrentUser = (currentUserId != null && user.id == currentUserId)
                    UserItem(
                        user = user,
                        isCurrentUser = isCurrentUser,
                        onEdit = {
                            vm.clearMessages()
                            selectedUser = user
                            showEditDialog = true
                        },
                        onDelete = {
                            vm.clearMessages()
                            selectedUser = user
                            showDeleteDialog = true
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                if (state.filteredUsers.isEmpty()) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(top = 32.dp), contentAlignment = Alignment.Center) {
                            Text("No se encontraron usuarios.", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }
    
    if (showEditDialog && selectedUser != null) {
        UserFormDialog(
            user = selectedUser,
            serverError = state.error,
            isLoading = state.isLoading,
            onDismiss = { 
                showEditDialog = false 
                vm.clearMessages()
            },
            onConfirm = { name, lastName, email, _ ->
                vm.updateUser(selectedUser!!.id, name, lastName, email, null) { success ->
                    if(success) showEditDialog = false
                }
            }
        )
    }

    if (showDeleteDialog && selectedUser != null) {
        AlertDialog(
            onDismissRequest = { if(!state.isLoading) showDeleteDialog = false },
            title = { Text("Eliminar Usuario") },
            text = { Text("¿Estás seguro de eliminar a ${selectedUser!!.name} ${selectedUser!!.lastName ?: ""}?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        vm.deleteUser(selectedUser!!.id)
                        showDeleteDialog = false
                    },
                    enabled = !state.isLoading
                ) {
                    if(state.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                    } else {
                        Text("Sí, eliminar", color = MaterialTheme.colorScheme.error)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }, enabled = !state.isLoading) {
                    Text("No") 
                }
            }
        )
    }
}

@Composable
fun UserManagementCreateContent(
    serverError: String?,
    isLoading: Boolean,
    onConfirm: (String, String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        UserFormFields(
            user = null,
            serverError = serverError,
            isLoading = isLoading,
            onConfirm = onConfirm,
            onCancel = onCancel
        )
    }
}

@Composable
fun UserFormFields(
    user: UserDto?,
    serverError: String? = null,
    isLoading: Boolean = false,
    onConfirm: (String, String, String, String) -> Unit,
    onCancel: () -> Unit
) {
    // Ahora podemos usar directamente las propiedades name y lastName
    // Si lastName es null (por ser opcional), usamos ""
    var name by remember { mutableStateOf(user?.name ?: "") }
    var lastName by remember { mutableStateOf(user?.lastName ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    
    var localError by remember { mutableStateOf<String?>(null) }
    
    val lettersRegex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$")
    val strictEmailRegex = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = name, onValueChange = { name = it }, label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isError = localError?.contains("Nombre") == true
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = lastName, onValueChange = { lastName = it }, label = { Text("Apellido") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isError = localError?.contains("Apellido") == true
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = email, onValueChange = { email = it }, label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading,
            isError = localError?.contains("Email") == true
        )
        
        if (user == null) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = password, 
                onValueChange = { password = it }, 
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                enabled = !isLoading,
                isError = localError?.contains("Contraseña") == true
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = confirmPassword, 
                onValueChange = { confirmPassword = it }, 
                label = { Text("Confirmar contraseña") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation(),
                enabled = !isLoading,
                isError = localError?.contains("coinciden") == true
            )
            
            if (password.isNotEmpty() && !isPasswordRobust(password)) {
                 Spacer(Modifier.height(4.dp))
                 Text(
                     text = "Debe tener 8+ caracteres, mayúscula, minúscula, número y símbolo.",
                     style = MaterialTheme.typography.bodySmall,
                     color = Color.Gray
                 )
            }
        }

        if (localError != null) {
            Spacer(Modifier.height(16.dp))
            Text(localError!!, color = MaterialTheme.colorScheme.error, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        } else if (serverError != null) {
            Spacer(Modifier.height(16.dp))
            Text(serverError, color = MaterialTheme.colorScheme.error, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        
        Spacer(Modifier.height(24.dp))
        
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = onCancel,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text("Cancelar")
            }
            Spacer(Modifier.width(16.dp))
            Button(
                onClick = {
                    localError = null
                    if (name.isBlank() || lastName.isBlank() || email.isBlank()) {
                        localError = "Campos obligatorios vacíos"
                        return@Button
                    }
                    if (!name.matches(lettersRegex) || !lastName.matches(lettersRegex)) {
                        localError = "Nombres y apellidos: solo letras y espacios"
                        return@Button
                    }
                    if (!email.matches(strictEmailRegex)) {
                        localError = "Email inválido (ej: usuario@dominio.com)"
                        return@Button
                    }
                    if (user == null) {
                        if (password.isBlank()) {
                            localError = "Contraseña obligatoria"
                            return@Button
                        }
                        if (!isPasswordRobust(password)) {
                            localError = "Contraseña débil o formato inválido"
                            return@Button
                        }
                        if (password != confirmPassword) {
                            localError = "Contraseñas no coinciden"
                            return@Button
                        }
                    }
                    onConfirm(name, lastName, email, password)
                },
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                } else {
                    Text("Guardar")
                }
            }
        }
    }
}

@Composable
fun UserFormDialog(
    user: UserDto?, 
    serverError: String? = null,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    AlertDialog(
        onDismissRequest = { if(!isLoading) onDismiss() },
        title = { Text(if (user == null) "Nuevo Usuario" else "Modificar Usuario") },
        text = {
             Column(Modifier.verticalScroll(rememberScrollState())) {
                 UserFormFields(
                     user = user,
                     serverError = serverError,
                     isLoading = isLoading,
                     onConfirm = onConfirm,
                     onCancel = onDismiss 
                 )
             }
        },
        confirmButton = {}, 
        dismissButton = {}
    )
}

@Composable
fun UserItem(
    user: UserDto,
    isCurrentUser: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${user.name} ${user.lastName ?: ""}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(text = user.email, fontSize = 14.sp, color = Color.Gray)
            }
            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Modificar", tint = MaterialTheme.colorScheme.primary)
            }
            
            if (!isCurrentUser) {
                IconButton(onClick = onDelete) {
                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            } else {
                Spacer(modifier = Modifier.width(48.dp)) 
            }
        }
    }
}
