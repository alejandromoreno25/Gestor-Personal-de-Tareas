package com.example.gestorpersonaldetareas.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestorpersonaldetareas.model.Tarea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareasScreen(
    tareas: List<Tarea>,
    onAgregarClick: () -> Unit,
    onCompletadaChange: (Tarea, Boolean) -> Unit,
    onEditarClick: (Tarea) -> Unit,
    onEliminarClick: (Tarea) -> Unit
) {
    // Creamos la estructura de la pantalla con Scaffold que te deja huecos
    // para elementos como FloatingActionButton y TopAppBar y así crear una pantalla
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
        // Comprobamos si la lista está vacía
        if (tareas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "No hay tareas pendientes. Pulsa + para añadir una.")
            }
        } else {
            // Si la lista no está vacía, mostramos las tareas con un LazyColumn
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingInterno),
                contentPadding = PaddingValues(16.dp)
            ) {
                // Iteramos sobre la lista de tareas y mostramos cada una con un TareaItem
                items(tareas) { tareaActual ->
                    TareaItem(tareaActual,
                        { onCompletadaChange(tareaActual, it) },
                        { onEditarClick(tareaActual) },
                        { onEliminarClick(tareaActual) })
                    //En Kotlin, cuando una función lambda recibe un solo parámetro
                // y ese parámetro no tiene un nombre
                // se puede usar la sintaxis it para hacer referencia al parámetro.
                }
            }
        }
    }
}