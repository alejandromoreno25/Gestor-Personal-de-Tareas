package com.example.gestorpersonaldetareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestorpersonaldetareas.model.Prioridad
import com.example.gestorpersonaldetareas.model.Tarea

@Composable
fun TareaDialog(
    tareaExistente: Tarea? = null, // Si es null, creamos; si trae una tarea, editamos
    onDismiss: () -> Unit,
    onConfirm: (Tarea) -> Unit
) {
    // Sirve para guadar el estado de los campos de texto en los que el usuario escribe
    var titulo by remember { mutableStateOf(tareaExistente?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(tareaExistente?.descripcion ?: "") }
    var prioridad by remember { mutableStateOf(tareaExistente?.prioridad ?: Prioridad.MEDIA) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (tareaExistente == null) "Nueva Tarea" else "Editar Tarea")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // Campo obligatorio: Título
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción (opcional)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Prioridad:",
                    style = MaterialTheme.typography.labelLarge
                )

                // Fila con los 3 botones seleccionables (BAJA, MEDIA, ALTA)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Prioridad.entries.forEach { opcion ->
                        FilterChip(
                            selected = (prioridad == opcion),
                            onClick = { prioridad = opcion },
                            label = { Text(opcion.name) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                // Solo activamos el botón Guardar si el título no está vacío
                enabled = titulo.isNotBlank(),
                onClick = {
                    val tareaGuardada = Tarea(
                        id = tareaExistente?.id ?: 0,
                        titulo = titulo.trim(),
                        descripcion = descripcion.trim(),
                        prioridad = prioridad,
                        completada = tareaExistente?.completada ?: false
                    )
                    onConfirm(tareaGuardada)
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}