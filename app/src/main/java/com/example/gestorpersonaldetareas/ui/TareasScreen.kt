package com.example.gestorpersonaldetareas.ui

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import com.example.gestorpersonaldetareas.viewmodel.FiltroTarea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareasScreen(
    tareas: List<Tarea>,
    filtroActual: FiltroTarea,
    onFiltroChange: (FiltroTarea) -> Unit,
    onAgregarClick: () -> Unit,
    onCompletadaChange: (Tarea, Boolean) -> Unit,
    onEditarClick: (Tarea) -> Unit,
    onEliminarClick: (Tarea) -> Unit
) {
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
        // Usamos Column para colocar la fila de filtros arriba y la lista debajo
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingInterno)
        ) {
            // Fila horizontal con los 3 botones de filtro
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FiltroTarea.entries.forEach { filtro ->
                    FilterChip(
                        // esto vale para saber que boton esta activo y que no lo esta
                        selected = filtro == filtroActual,
                        onClick = {
                            onFiltroChange(filtro)
                        },
                        label = { Text(filtro.etiqueta) }
                    )
                }
            }

            if (tareas.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val mensajeVacio = when (filtroActual) {
                        FiltroTarea.TODAS -> "No hay tareas pendientes. Pulsa + para añadir una."
                        FiltroTarea.PENDIENTES -> "No tienes tareas pendientes."
                        FiltroTarea.COMPLETADAS -> "Aún no has completado ninguna tarea."
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
                            onEliminarClick = { onEliminarClick(tareaActual) }
                        )
                    }
                }
            }
        }
    }
}