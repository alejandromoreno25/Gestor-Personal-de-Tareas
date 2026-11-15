package com.example.gestorpersonaldetareas.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.gestorpersonaldetareas.model.Tarea

@Composable
fun TareaItem(
    tarea: Tarea,
    onCompletadaChange: (Boolean) -> Unit,
    onEditarClick: () -> Unit,
    onEliminarClick: () -> Unit,
    onCompartirClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = tarea.completada,
                onCheckedChange = { nuevoValor -> onCompletadaChange(nuevoValor) }
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = tarea.titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (tarea.completada) TextDecoration.LineThrough else TextDecoration.None
                )

                if (tarea.descripcion.isNotBlank()) {
                    Text(
                        text = tarea.descripcion,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Text(
                    text = "Prioridad: ${tarea.prioridad}",
                    style = MaterialTheme.typography.labelMedium
                )
            }

            IconButton(onClick = onCompartirClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Compartir tarea"
                )
            }

            IconButton(onClick = onEditarClick) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar tarea"
                )
            }

            IconButton(onClick = onEliminarClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar tarea"
                )
            }
        }
    }
}