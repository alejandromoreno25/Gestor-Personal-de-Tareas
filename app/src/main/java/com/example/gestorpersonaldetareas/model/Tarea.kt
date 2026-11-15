package com.example.gestorpersonaldetareas.model

enum class Prioridad {
    BAJA,
    MEDIA,
    ALTA
}
data class Tarea(
    val id: Int = 0,
    val titulo: String,
    val descripcion: String = "",
    val prioridad: Prioridad,
    val completada: Boolean = false
)