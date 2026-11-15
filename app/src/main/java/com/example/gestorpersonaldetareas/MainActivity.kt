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
import com.example.gestorpersonaldetareas.ui.TareaDialog
import com.example.gestorpersonaldetareas.ui.TareasScreen
import com.example.gestorpersonaldetareas.ui.theme.GestorPersonalDeTareasTheme
import com.example.gestorpersonaldetareas.viewmodel.TareaViewModel
import com.example.gestorpersonaldetareas.viewmodel.TareaViewModelFactory

class MainActivity : ComponentActivity() {

    // Inicializamos el ViewModel pasándole el DAO de nuestra base de datos Room
    private val viewModel: TareaViewModel by viewModels {
        val database = AppDatabase.obtenerInstancia(applicationContext)
        TareaViewModelFactory(database.tareaDao())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestorPersonalDeTareasTheme {
                // Observamos el StateFlow del ViewModel
                val listaTareas by viewModel.listaTareas.collectAsState()
                val filtroActual by viewModel.filtroActual.collectAsState()
                // Actualizamos el StateFlow del ViewModel con el filtro elegido por el usuario

                var mostrarDialogo by remember { mutableStateOf(false) }
                var tareaAEditar by remember { mutableStateOf<Tarea?>(null) }

                TareasScreen(
                    tareas = listaTareas,
                    filtroActual = filtroActual,
                    onFiltroChange = { viewModel.cambiarFiltro(it) },
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