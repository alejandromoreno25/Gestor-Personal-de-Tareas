package com.example.gestorpersonaldetareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.example.gestorpersonaldetareas.data.AppDatabase
import com.example.gestorpersonaldetareas.model.Tarea
import com.example.gestorpersonaldetareas.ui.TareaDialog
import com.example.gestorpersonaldetareas.ui.TareasScreen
import com.example.gestorpersonaldetareas.ui.theme.GestorPersonalDeTareasTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestorPersonalDeTareasTheme {
                // Obtenemos la instancia única de la base de datos y su DAO
                val context = LocalContext.current
                val db = remember { AppDatabase.obtenerInstancia(context) }
                val tareaDao = remember { db.tareaDao() }

                //Observamos en tiempo real las tareas guardadas en SQLite
                val listaTareas by tareaDao.obtenerTodas().collectAsState(initial = emptyList())

                //Ámbito de corrutinas para ejecutar operaciones de escritura en segundo plano
                val scope = rememberCoroutineScope()

                // Estados del formulario emergente
                var mostrarDialogo by remember { mutableStateOf(false) }
                var tareaAEditar by remember { mutableStateOf<Tarea?>(null) }

                TareasScreen(
                    tareas = listaTareas,
                    onAgregarClick = {
                        tareaAEditar = null
                        mostrarDialogo = true
                    },
                    onCompletadaChange = { tarea, nuevoEstado ->
                        scope.launch {
                            tareaDao.actualizar(tarea.copy(completada = nuevoEstado))
                            // Actualiza la tarea en Room
                        // cambiando su estado 'completada'.
                        }
                    },
                    onEditarClick = { tarea ->
                        tareaAEditar = tarea
                        mostrarDialogo = true
                    },
                    onEliminarClick = { tarea ->
                        scope.launch {
                            tareaDao.eliminar(tarea)
                            // Elimina la tarea de la base de datos Room
                        }
                    }
                )

                if (mostrarDialogo) {
                    TareaDialog(
                        tareaExistente = tareaAEditar,
                        onDismiss = { mostrarDialogo = false },
                        onConfirm = { tareaGuardada ->
                            scope.launch {
                                if (tareaAEditar == null) {
                                    tareaDao.insertar(tareaGuardada)
                                    //Inserta la nueva tarea en Room
                                } else {
                                    tareaDao.actualizar(tareaGuardada)
                                }
                            }
                            mostrarDialogo = false
                        }
                    )
                }
            }
        }
    }
}