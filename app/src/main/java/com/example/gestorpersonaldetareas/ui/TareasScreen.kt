package com.example.gestorpersonaldetareas.ui

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.gestorpersonaldetareas.model.Tarea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareasScreen(
    tareas: List<Tarea>,
    mostrarSoloPendientes: Boolean,
    onMostrarSoloPendientesChange: (Boolean) -> Unit,
    onAgregarClick: () -> Unit,
    onCompletadaChange: (Tarea, Boolean) -> Unit,
    onEditarClick: (Tarea) -> Unit,
    onEliminarClick: (Tarea) -> Unit
) {
    val context = LocalContext.current
    var tareaAEliminar by remember { mutableStateOf<Tarea?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Tareas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregarClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Nueva tarea"
                )
            }
        }
    ) { paddingInterno ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
        ) {
            // Fila con el Switch exigido en el apartado 3.5
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Mostrar solo pendientes",
                    style = MaterialTheme.typography.bodyLarge
                )
                Switch(
                    checked = mostrarSoloPendientes,
                    onCheckedChange = onMostrarSoloPendientesChange
                )
            }

            if (tareas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val mensajeVacio = if (mostrarSoloPendientes) {
                        "No tienes tareas pendientes."
                    } else {
                        "No hay tareas registradas. Pulsa + para añadir una."
                    }
                    Text(text = mensajeVacio)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(tareas, key = { it.id }) { tareaActual ->
                        TareaItem(
                            tarea = tareaActual,
                            onCompletadaChange = { onCompletadaChange(tareaActual, it) },
                            onEditarClick = { onEditarClick(tareaActual) },
                            onEliminarClick = { tareaAEliminar = tareaActual },
                            onCompartirClick = { compartirTarea(context, tareaActual) }
                        )
                    }
                }
            }
        }

        tareaAEliminar?.let { tarea ->
            AlertDialog(
                onDismissRequest = { tareaAEliminar = null },
                title = { Text("Eliminar tarea") },
                text = { Text("¿Estás seguro de que deseas eliminar la tarea «${tarea.titulo}»?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEliminarClick(tarea)
                            tareaAEliminar = null
                        }
                    ) {
                        Text("Eliminar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { tareaAEliminar = null }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

private fun compartirTarea(context: Context, tarea: Tarea) {
    val textoCompartir = buildString {
        appendLine("Tarea: ${tarea.titulo}")
        if (tarea.descripcion.isNotBlank()) {
            appendLine("Descripción: ${tarea.descripcion}")
        }
        append("Prioridad: ${tarea.prioridad.name}")
    }

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, tarea.titulo)
        putExtra(Intent.EXTRA_TEXT, textoCompartir)
    }

    val chooser = Intent.createChooser(sendIntent, "Compartir tarea mediante")
    try {
        context.startActivity(chooser)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "No hay aplicaciones disponibles para compartir",
            Toast.LENGTH_SHORT
        ).show()
    }
}