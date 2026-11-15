package com.example.gestorpersonaldetareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import com.example.gestorpersonaldetareas.model.Prioridad
import com.example.gestorpersonaldetareas.model.Tarea
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

                // Conectamos nuestra pantalla principal
                TareasScreen(
                    tareas = listaTareas,
                    onAgregarClick = {
                        // Lo programaremos en el siguiente paso (formulario de creación)
                    },
                    onCompletadaChange = { tarea, nuevoEstado ->
                        // Buscamos en qué posición de la lista está esa tarea
                        val indice = listaTareas.indexOf(tarea)
                        if (indice != -1) {
                            listaTareas[indice] = tarea.copy(completada = nuevoEstado)
                        }
                    },
                    onEditarClick = { tarea ->
                    },
                    onEliminarClick = { tarea ->
                        listaTareas.remove(tarea)
                        // eliminamos la tarea de la lista observable y la notificamos a la UI
                    }
                )
            }
        }
    }
}