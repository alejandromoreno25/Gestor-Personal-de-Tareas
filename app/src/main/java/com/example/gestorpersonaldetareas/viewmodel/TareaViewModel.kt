package com.example.gestorpersonaldetareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gestorpersonaldetareas.data.TareaDao
import com.example.gestorpersonaldetareas.model.Tarea
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class FiltroTarea(val etiqueta: String) {
    TODAS("Todas"),
    PENDIENTES("Pendientes"),
    COMPLETADAS("Completadas")
}

class TareaViewModel(private val tareaDao: TareaDao) : ViewModel() {

    // Estado privado mutable y estado público de solo lectura para el filtro activo
    private val _filtroActual = MutableStateFlow(FiltroTarea.TODAS)
    val filtroActual: StateFlow<FiltroTarea> = _filtroActual.asStateFlow()

    // Combinamos en tiempo real las tareas de Room con el filtro elegido
    val listaTareas: StateFlow<List<Tarea>> = combine(
        tareaDao.obtenerTodas(),
        _filtroActual
    ) { tareas, filtro ->
        when (filtro) {
            FiltroTarea.TODAS -> tareas
            FiltroTarea.PENDIENTES -> tareas.filter { !it.completada }
            FiltroTarea.COMPLETADAS -> tareas.filter { it.completada }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun cambiarFiltro(nuevoFiltro: FiltroTarea) {
        _filtroActual.value = nuevoFiltro

    }

    fun agregarTarea(tarea: Tarea) {
        viewModelScope.launch {
            tareaDao.insertar(tarea)
        }
    }

    fun actualizarTarea(tarea: Tarea) {
        viewModelScope.launch {
            tareaDao.actualizar(tarea)
        }
    }

    fun cambiarEstadoCompletada(tarea: Tarea, completada: Boolean) {
        viewModelScope.launch {
            tareaDao.actualizar(tarea.copy(completada = completada))
        }
    }

    fun eliminarTarea(tarea: Tarea) {
        viewModelScope.launch {
            tareaDao.eliminar(tarea)
        }
    }
}

class TareaViewModelFactory(private val tareaDao: TareaDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TareaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TareaViewModel(tareaDao) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}