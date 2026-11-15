package com.example.gestorpersonaldetareas.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TareaDialog(
    tareaExistente: Tarea? = null,
    onDismiss: () -> Unit,
    onConfirm: (Tarea) -> Unit
) {
    var titulo by remember { mutableStateOf(tareaExistente?.titulo ?: "") }
    var descripcion by remember { mutableStateOf(tareaExistente?.descripcion ?: "") }
    var prioridad by remember { mutableStateOf(tareaExistente?.prioridad ?: Prioridad.MEDIA) }

    var menuDesplegado by remember { mutableStateOf(false) }
    var errorTitulo by remember { mutableStateOf<String?>(null) }
    var guardando by remember { mutableStateOf(false) }

    fun validarTitulo(texto: String): String? {
        val limpio = texto.trim()
        return when {
            limpio.isEmpty() -> "El título no puede estar vacío"
            limpio.length < 3 -> "El título debe tener al menos 3 caracteres"
            else -> null
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = if (tareaExistente == null) "Nueva Tarea" else "Editar Tarea")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = {
                        titulo = it
                        if (errorTitulo != null) {
                            errorTitulo = validarTitulo(it)
                        }
                    },
                    label = { Text("Título *") },
                    isError = errorTitulo != null,
                    supportingText = {
                        if (errorTitulo != null) {
                            Text(
                                text = errorTitulo!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
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

                ExposedDropdownMenuBox(
                    expanded = menuDesplegado,
                    onExpandedChange = { menuDesplegado = it }
                ) {
                    OutlinedTextField(
                        value = prioridad.name,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Prioridad") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = menuDesplegado)
                        },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = menuDesplegado,
                        onDismissRequest = { menuDesplegado = false }
                    ) {
                        Prioridad.entries.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.name) },
                                onClick = {
                                    prioridad = opcion
                                    menuDesplegado = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                enabled = !guardando,
                onClick = {
                    val error = validarTitulo(titulo)
                    if (error != null) {
                        errorTitulo = error
                    } else {
                        guardando = true
                        val tareaGuardada = Tarea(
                            id = tareaExistente?.id ?: 0,
                            titulo = titulo.trim(),
                            descripcion = descripcion.trim(),
                            prioridad = prioridad,
                            completada = tareaExistente?.completada ?: false
                        )
                        onConfirm(tareaGuardada)
                    }
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