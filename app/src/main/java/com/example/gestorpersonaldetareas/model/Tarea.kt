package com.example.gestorpersonaldetareas.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Prioridad {
    BAJA,
    MEDIA,
    ALTA
}
@Entity(tableName = "tareas")
// Toda tabla necesita una clave primaria que
// identifique de forma única a cada fila.
// Al poner autoGenerate = true sobre id,
// SQLite generará los números 1, 2, 3...
// automáticamente cada vez que guardemos una tarea
data class Tarea(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val descripcion: String = "",
    val prioridad: Prioridad,
    val completada: Boolean = false
)