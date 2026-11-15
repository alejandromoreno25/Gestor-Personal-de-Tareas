package com.example.gestorpersonaldetareas.preferencias

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Crea una única instancia de DataStore asociada al contexto de la aplicación
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "preferencias_tareas")

class PreferenciasDataStore(private val context: Context) {

    companion object {
        private val SOLO_PENDIENTES_KEY = booleanPreferencesKey("mostrar_solo_pendientes")
    }

    // Flujo reactivo que emite el valor guardado (por defecto false = mostrar todas)
    val mostrarSoloPendientes: Flow<Boolean> = context.dataStore.data
        .map { preferencias ->
            preferencias[SOLO_PENDIENTES_KEY] ?: false
        }

    // Guarda el nuevo estado del Switch de forma asíncrona en disco
    suspend fun guardarMostrarSoloPendientes(soloPendientes: Boolean) {
        context.dataStore.edit { preferencias ->
            preferencias[SOLO_PENDIENTES_KEY] = soloPendientes
        }
    }
}