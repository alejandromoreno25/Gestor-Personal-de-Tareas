package com.example.gestorpersonaldetareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.gestorpersonaldetareas.data.AppDatabase
import com.example.gestorpersonaldetareas.model.Tarea
import com.example.gestorpersonaldetareas.preferencias.PreferenciasDataStore
import com.example.gestorpersonaldetareas.ui.TareaDialog
import com.example.gestorpersonaldetareas.ui.TareasEjemploScreen
import com.example.gestorpersonaldetareas.ui.TareasScreen
import com.example.gestorpersonaldetareas.ui.theme.GestorPersonalDeTareasTheme
import com.example.gestorpersonaldetareas.viewmodel.TareaViewModel
import com.example.gestorpersonaldetareas.viewmodel.TareaViewModelFactory

class MainActivity : ComponentActivity() {

    private val viewModel: TareaViewModel by viewModels {
        val database = AppDatabase.obtenerInstancia(applicationContext)
        val preferencias = PreferenciasDataStore(applicationContext)
        TareaViewModelFactory(database.tareaDao(), preferencias)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestorPersonalDeTareasTheme {
                val listaTareas by viewModel.listaTareas.collectAsState()
                val mostrarSoloPendientes by viewModel.mostrarSoloPendientes.collectAsState()
                val estadoApi by viewModel.estadoApi.collectAsState()

                var pestanaSeleccionada by rememberSaveable { mutableIntStateOf(0) }
                var mostrarDialogo by remember { mutableStateOf(false) }
                var tareaAEditar by remember { mutableStateOf<Tarea?>(null) }

                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = pestanaSeleccionada == 0,
                                onClick = { pestanaSeleccionada = 0 },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = "Mis tareas"
                                    )
                                },
                                label = { Text("Mis Tareas") }
                            )
                            NavigationBarItem(
                                selected = pestanaSeleccionada == 1,
                                onClick = { pestanaSeleccionada = 1 },
                                icon = {
                                    Icon(
                                        imageVector = Icons.Default.Refresh,
                                        contentDescription = "Tareas de ejemplo"
                                    )
                                },
                                label = { Text("Tareas de ejemplo") }
                            )
                        }
                    }
                ) { paddingBarraInferior ->
                    if (pestanaSeleccionada == 0) {
                        TareasScreen(
                            tareas = listaTareas,
                            mostrarSoloPendientes = mostrarSoloPendientes,
                            onMostrarSoloPendientesChange = { viewModel.cambiarMostrarSoloPendientes(it) },
                            onAgregarClick = {
                                tareaAEditar = null
                                mostrarDialogo = true
                            },
                            onCompletadaChange = { tarea, nuevoEstado ->
                                viewModel.cambiarEstadoCompletada(tarea, nuevoEstado)
                            },
                            onEditarClick = { tarea ->
                                tareaAEditar = tarea
                                mostrarDialogo = true
                            },
                            onEliminarClick = { tarea ->
                                viewModel.eliminarTarea(tarea)
                            },
                            modifier = Modifier.padding(paddingBarraInferior)
                        )
                    } else {
                        TareasEjemploScreen(
                            estadoApi = estadoApi,
                            onCargarClick = { viewModel.cargarTareasEjemplo() },
                            modifier = Modifier.padding(paddingBarraInferior)
                        )
                    }

                    if (mostrarDialogo) {
                        TareaDialog(
                            tareaExistente = tareaAEditar,
                            onDismiss = { mostrarDialogo = false },
                            onConfirm = { tareaGuardada ->
                                if (tareaAEditar == null) {
                                    viewModel.agregarTarea(tareaGuardada)
                                } else {
                                    viewModel.actualizarTarea(tareaGuardada)
                                }
                                mostrarDialogo = false
                            }
                        )
                    }
                }
            }
        }
    }
}