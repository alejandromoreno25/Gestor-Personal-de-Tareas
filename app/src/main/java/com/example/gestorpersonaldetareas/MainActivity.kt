package com.example.gestorpersonaldetareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.gestorpersonaldetareas.model.Prioridad
import com.example.gestorpersonaldetareas.model.Tarea
import com.example.gestorpersonaldetareas.ui.TareaDialog
import com.example.gestorpersonaldetareas.ui.TareasScreen
import com.example.gestorpersonaldetareas.ui.theme.GestorPersonalDeTareasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GestorPersonalDeTareasTheme {
                // Creamos una lista observable con 2 tareas de prueba
                val listaTareas = remember {
                    mutableStateListOf(
                        Tarea(
                            id = 1,
                            titulo = "Configurar proyecto en Android Studio",
                            descripcion = "Crear paquetes model y ui",
                            prioridad = Prioridad.ALTA,
                            completada = true
                        ),
                        Tarea(
                            id = 2,
                            titulo = "Probar interfaz en el emulador",
                            descripcion = "Verificar que se ven las tarjetas",
                            prioridad = Prioridad.MEDIA,
                            completada = false
                        )
                    )
                }

                // Estados para controlar cuándo se ve el formulario y qué tarea se edita
                var mostrarDialogo by remember { mutableStateOf(false) }
                var tareaAEditar by remember { mutableStateOf<Tarea?>(null) }
                var siguienteId by remember { mutableIntStateOf(3) }

                // Conectamos nuestra pantalla principal
                TareasScreen(
                    tareas = listaTareas,
                    onAgregarClick = {
                        tareaAEditar = null
                        mostrarDialogo = true
                        //Prepara los estados para crear una tarea nueva
                    },
                    onCompletadaChange = { tarea, nuevoEstado ->
                        val indice = listaTareas.indexOf(tarea)
                        if (indice != -1) {
                            listaTareas[indice] = tarea.copy(completada = nuevoEstado)
                        }
                    },
                    onEditarClick = { tarea ->
                        tareaAEditar = tarea
                        mostrarDialogo = true
                //Prepara los estados para editar una tarea existente:
                    },
                    onEliminarClick = { tarea ->
                        listaTareas.remove(tarea)
                    }
                )

                // Si mostrarDialogo es true, dibujamos el formulario encima
                if (mostrarDialogo) {
                    TareaDialog(
                        tareaExistente = tareaAEditar,
                        onDismiss = { mostrarDialogo = false },
                        onConfirm = { tareaGuardada ->
                            if (tareaAEditar == null) {
                                // Tarea nueva: le asignamos un ID único y la añadimos
                                listaTareas.add(tareaGuardada.copy(id = siguienteId))
                                siguienteId++
                            } else {
                                // Tarea editada: buscamos la antigua por su ID y la sustituimos
                                val indice = listaTareas.indexOfFirst { it.id == tareaGuardada.id }
                                if (indice != -1) {
                                    listaTareas[indice] = tareaGuardada
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