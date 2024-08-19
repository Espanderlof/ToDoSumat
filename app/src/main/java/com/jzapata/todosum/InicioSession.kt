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

    NavHost(navController = navController, startDestination = "inicioSesion") {
        composable("inicioSesion") {
            InicioSessionScreen(
                onNavigateToCrearCuenta = { navController.navigate("crearCuenta") },
                onNavigateToRecuperarPassword = { navController.navigate("recuperarPassword") }
            )
        }
        composable("crearCuenta") {
            CrearCuentaScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioSessionScreen(
    onNavigateToCrearCuenta: () -> Unit,
    onNavigateToRecuperarPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logotipo
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de correo electrónico
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

        // Campo de contraseña
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

        // Botón de iniciar sesión
        Button(
            onClick = { /* TODO: Implementar lógica de inicio de sesión */ },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Iniciar sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Crear cuenta (centrado)
        TextButton(
            onClick = onNavigateToCrearCuenta,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Crear cuenta")
        }

        // ¿Has olvidado la contraseña? (debajo de Crear cuenta)
        TextButton(
            onClick = onNavigateToRecuperarPassword,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("¿Has olvidado la contraseña?")
        }
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    heightDp = 640,
    name = "InicioSession Preview"
)
@Composable
fun InicioSessionPreview() {
    ToDoSumatTheme {
        InicioSessionScreen(
            onNavigateToCrearCuenta = {},
            onNavigateToRecuperarPassword = {}
        )
    }
}