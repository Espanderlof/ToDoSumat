package com.jzapata.todosum

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jzapata.todosum.ui.theme.ToDoSumatTheme

class InicioSession : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthManager.init(this)
        setContent {
            ToDoSumatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val startDestination = if (AuthManager.isLoggedIn()) "listaTareas" else "inicioSesion"

    NavHost(navController = navController, startDestination = startDestination) {
        composable("inicioSesion") {
            InicioSessionScreen(
                onNavigateToCrearCuenta = { navController.navigate("crearCuenta") },
                onNavigateToRecuperarPassword = { navController.navigate("recuperarPassword") },
                onNavigateToListaTareas = { navController.navigate("listaTareas") {
                    popUpTo("inicioSesion") { inclusive = true }
                }}
            )
        }
        composable("crearCuenta") {
            CrearCuentaScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("listaTareas") {
            ListaTareasScreen(
                onLogout = {
                    AuthManager.logout()
                    navController.navigate("inicioSesion") {
                        popUpTo("listaTareas") { inclusive = true }
                    }
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioSessionScreen(
    onNavigateToCrearCuenta: () -> Unit,
    onNavigateToRecuperarPassword: () -> Unit,
    onNavigateToListaTareas: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showErrorDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (AuthManager.login(email, password)) {
                    context.vibrateSuccess()
                    onNavigateToListaTareas()
                } else {
                    context.vibrateError()
                    showErrorDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onNavigateToCrearCuenta,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Crear cuenta")
        }

        TextButton(
            onClick = onNavigateToRecuperarPassword,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿Has olvidado la contraseña?")
        }
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error de inicio de sesión") },
            text = { Text("Email o contraseña incorrectos.") },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640, name = "InicioSession Preview")
@Composable
fun InicioSessionPreview() {
    ToDoSumatTheme {
        InicioSessionScreen(
            onNavigateToCrearCuenta = {},
            onNavigateToRecuperarPassword = {},
            onNavigateToListaTareas = {}
        )
    }
}