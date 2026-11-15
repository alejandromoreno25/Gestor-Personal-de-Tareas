package com.example.gestorpersonaldetareas.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gestorpersonaldetareas.model.Tarea
import kotlinx.coroutines.flow.Flow

@Dao
interface TareaDao {

    //Obtiene todas las tareas ordenadas de la más reciente a la más antigua
    @Query("SELECT * FROM tareas ORDER BY id DESC")
    fun obtenerTodas(): Flow<List<Tarea>>

    // Inserta una tarea nueva en la tabla
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(tarea: Tarea)

    //
    @Update
    suspend fun actualizar(tarea: Tarea)

    @Delete
    suspend fun eliminar(tarea: Tarea)

}