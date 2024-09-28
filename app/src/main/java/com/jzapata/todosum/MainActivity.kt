package com.jzapata.todosum

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jzapata.todosum.ui.theme.ToDoSumatTheme
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            try {
                AuthManager.init(this@MainActivity)
                setContent {
                    ToDoSumatTheme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            MainNavigation()
                        }
                    }
                }
            } catch (e: Exception) {
                // Manejar el error de inicialización aquí
                // Por ejemplo, mostrar un mensaje de error al usuario
                e.printStackTrace()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = if (AuthManager.isLoggedIn()) "listaTareas" else "inicioSession") {
        composable("inicioSession") {
            InicioSessionScreen(
                onNavigateToCrearCuenta = { navController.navigate("crearCuenta") },
                onNavigateToRecuperarPassword = { navController.navigate("recuperarPassword") },
                onNavigateToListaTareas = {
                    navController.navigate("listaTareas") {
                        popUpTo("inicioSession") { inclusive = true }
                    }
                }
            )
        }
        composable("crearCuenta") {
            CrearCuentaScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("recuperarPassword") {
            RecuperarPasswordScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("listaTareas") {
            ListaTareasScreen(
                onLogout = {
                    AuthManager.logout()
                    navController.navigate("inicioSession") {
                        popUpTo("listaTareas") { inclusive = true }
                    }
                }
            )
        }
    }
}