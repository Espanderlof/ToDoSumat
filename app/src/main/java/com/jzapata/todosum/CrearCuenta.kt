package com.jzapata.todosum

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearCuentaScreen(onNavigateBack: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var apellidos by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Crear cuenta",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            label = { Text("Apellidos") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo electrónico") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = fechaNacimiento?.let { dateFormatter.format(Date(it)) } ?: "",
            onValueChange = { },
            label = { Text("Fecha de nacimiento") },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(Icons.Filled.DateRange, contentDescription = "Seleccionar fecha")
                }
            }
        )

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        fechaNacimiento = datePickerState.selectedDateMillis
                        showDatePicker = false
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePicker = false }) {
                        Text("Cancelar")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (validarCampos(nombre, apellidos, email, password, fechaNacimiento)) {
                    coroutineScope.launch {
                        try {
                            val result = AuthManager.createAccount(email, password, "$nombre $apellidos")
                            if (result) {
                                context.vibrateSuccess()
                                showConfirmDialog = true
                            } else {
                                context.vibrateError()
                                showErrorDialog = true
                            }
                        } catch (e: Exception) {
                            context.vibrateError()
                            showErrorDialog = true
                        }
                    }
                } else {
                    context.vibrateError()
                    showErrorDialog = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Crear cuenta")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onNavigateBack) {
            Text("Volver al inicio de sesión")
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Cuenta creada") },
            text = { Text("Tu cuenta ha sido creada con éxito.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onNavigateBack()
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    if (showErrorDialog) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = false },
            title = { Text("Error") },
            text = { Text("Este correo electrónico ya está en uso.") },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}

fun validarCampos(nombre: String, apellidos: String, email: String, password: String, fechaNacimiento: Long?): Boolean {
    return nombre.isNotBlank() && apellidos.isNotBlank() && email.isNotBlank() &&
            password.isNotBlank() && fechaNacimiento != null
    // Para futuras validaciones
}

@Preview(showBackground = true, widthDp = 320, heightDp = 640)
@Composable
fun CrearCuentaScreenPreview() {
    MaterialTheme {
        CrearCuentaScreen(onNavigateBack = {})
    }
}