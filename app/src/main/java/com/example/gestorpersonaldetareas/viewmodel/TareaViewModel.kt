package com.example.gestorpersonaldetareas.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gestorpersonaldetareas.comunicaciones.RetrofitClient
import com.example.gestorpersonaldetareas.comunicaciones.TareaEjemplo
import com.example.gestorpersonaldetareas.data.TareaDao
import com.example.gestorpersonaldetareas.model.Tarea
import com.example.gestorpersonaldetareas.preferencias.PreferenciasDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface EstadoApi {
    data object Inicial : EstadoApi
    data object Cargando : EstadoApi
    data class Exito(val tareas: List<TareaEjemplo>) : EstadoApi
    data class Error(val mensaje: String) : EstadoApi
}

class TareaViewModel(
    private val tareaDao: TareaDao,
    private val preferencias: PreferenciasDataStore
) : ViewModel() {

    val mostrarSoloPendientes: StateFlow<Boolean> = preferencias.mostrarSoloPendientes
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val listaTareas: StateFlow<List<Tarea>> = combine(
        tareaDao.obtenerTodas(),
        preferencias.mostrarSoloPendientes
    ) { tareas, soloPendientes ->
        if (soloPendientes) {
            tareas.filter { !it.completada }
        } else {
            tareas
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _estadoApi = MutableStateFlow<EstadoApi>(EstadoApi.Inicial)
    val estadoApi: StateFlow<EstadoApi> = _estadoApi.asStateFlow()

    fun cargarTareasEjemplo() {
        if (_estadoApi.value is EstadoApi.Cargando) return

        viewModelScope.launch {
            _estadoApi.value = EstadoApi.Cargando
            try {
                val resultado = RetrofitClient.apiService.obtenerTareasEjemplo(userId = 1)
                _estadoApi.value = EstadoApi.Exito(resultado)
            } catch (e: Exception) {
                _estadoApi.value = EstadoApi.Error(
                    "No se pudo conectar con el servidor. Comprueba tu conexión a Internet."
                )
            }
        }
    }

    fun cambiarMostrarSoloPendientes(soloPendientes: Boolean) {
        viewModelScope.launch {
            preferencias.guardarMostrarSoloPendientes(soloPendientes)
        }
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

class TareaViewModelFactory(
    private val tareaDao: TareaDao,
    private val preferencias: PreferenciasDataStore
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TareaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TareaViewModel(tareaDao, preferencias) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}