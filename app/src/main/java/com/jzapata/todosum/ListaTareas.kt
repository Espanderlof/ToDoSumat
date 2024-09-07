package com.jzapata.todosum

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class Tarea(
    val id: Int,
    val nombre: String,
    val fechaCreacion: Date,
    val fechaLimite: Date,
    var fechaCompletada: Date? = null,
    var completada: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaTareasScreen() {
    val context = LocalContext.current
    var showAddDialog by remember { mutableStateOf(false) }
    var tareas by remember {
        mutableStateOf(
            listOf(
                Tarea(1, "Tarea 1", Date(), Date(Date().time + 86400000)),
                Tarea(2, "Tarea 2", Date(), Date(Date().time + 172800000), Date(), true),
                Tarea(3, "Tarea 3", Date(), Date(Date().time + 259200000))
            )
        )
    }

    fun agregarTarea(nombre: String) {
        val nuevaTarea = Tarea(
            id = tareas.size + 1,
            nombre = nombre,
            fechaCreacion = Date(),
            fechaLimite = Date(Date().time + 86400000) // 1 día después
        )
        tareas = tareas + nuevaTarea
        context.vibrateSuccess()
    }

    fun completarTarea(tarea: Tarea) {
        tareas = tareas.map {
            if (it.id == tarea.id) {
                it.copy(completada = true, fechaCompletada = Date())
            } else {
                it
            }
        }
        context.vibrateSuccess()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.height(40.dp)
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // agregar nueva tarea
                    showAddDialog = true
                },
                content = {
                    Icon(Icons.Filled.Add, contentDescription = "Agregar tarea")
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            item {
                Text(
                    "Tareas por completar",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            val tareasPorCompletar = tareas.filter { !it.completada }
            if (tareasPorCompletar.isEmpty()) {
                item {
                    Text("Todas las tareas están completadas")
                }
            } else {
                items(tareasPorCompletar) { tarea ->
                    TareaItem(tarea, onCompletarTarea = { completarTarea(it) })
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "Tareas completadas",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                )
            }

            val tareasCompletadas = tareas.filter { it.completada }
            if (tareasCompletadas.isEmpty()) {
                item {
                    Text("No hay tareas completadas")
                }
            } else {
                items(tareasCompletadas) { tarea ->
                    TareaCompletadaItem(tarea)
                }
            }
        }
    }

    if (showAddDialog) {
        AgregarTareaDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { nombreTarea ->
                agregarTarea(nombreTarea)
                showAddDialog = false
            }
        )
    }

}

@Composable
fun TareaItem(tarea: Tarea, onCompletarTarea: (Tarea) -> Unit) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = tarea.completada,
            onCheckedChange = {
                // Completar tarea
                if (it) onCompletarTarea(tarea)
            }
        )
        Column {
            Text(tarea.nombre, style = MaterialTheme.typography.bodyLarge)
            Text(
                "Fecha ingreso: ${dateFormat.format(tarea.fechaCreacion)}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                "Fecha límite: ${dateFormat.format(tarea.fechaLimite)}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun TareaCompletadaItem(tarea: Tarea) {
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(8.dp)
    ) {
        Text(tarea.nombre, style = MaterialTheme.typography.bodyLarge)
        Text(
            "Fecha ingreso: ${dateFormat.format(tarea.fechaCreacion)}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            "Fecha completada: ${dateFormat.format(tarea.fechaCompletada)}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgregarTareaDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var nombreTarea by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar nueva tarea") },
        text = {
            TextField(
                value = nombreTarea,
                onValueChange = { nombreTarea = it },
                label = { Text("Nombre de la tarea") }
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    if (nombreTarea.isNotBlank()) {
                        onConfirm(nombreTarea)
                    }
                }
            ) {
                Text("Agregar")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ListaTareasScreenPreview() {
    MaterialTheme {
        ListaTareasScreen()
    }
}