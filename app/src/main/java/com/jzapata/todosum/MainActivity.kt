package com.jzapata.todosum

import android.os.Bundle
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "inicioSession") {
        composable("inicioSession") {
            InicioSessionScreen(
                onNavigateToCrearCuenta = { navController.navigate("crearCuenta") },
                onNavigateToRecuperarPassword = { navController.navigate("recuperarPassword") },
                onNavigateToListaTareas = { navController.navigate("listaTareas") }
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
            ListaTareasScreen()
        }
    }
}