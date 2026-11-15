package com.example.gestorpersonaldetareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.gestorpersonaldetareas.data.AppDatabase
import com.example.gestorpersonaldetareas.model.Tarea
import com.example.gestorpersonaldetareas.preferencias.PreferenciasDataStore
import com.example.gestorpersonaldetareas.ui.TareaDialog
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

                var mostrarDialogo by remember { mutableStateOf(false) }
                var tareaAEditar by remember { mutableStateOf<Tarea?>(null) }

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
                    }
                )

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