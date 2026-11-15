package com.example.gestorpersonaldetareas.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gestorpersonaldetareas.model.Tarea

@Database(entities = [Tarea::class], version = 1, exportSchema = false)
// esto sirve para decirle a room que es una base de datos de tareas
abstract class AppDatabase : RoomDatabase() {

    // Declara aquí el metodo abstracto que devuelve el DAO:
    abstract fun tareaDao(): TareaDao

    companion object {
        // @Volatile asegura que el valor de INSTANCIA esté siempre actualizado entre hilos
        @Volatile
        private var INSTANCIA: AppDatabase? = null

        fun obtenerInstancia(context: Context): AppDatabase {
            // Si ya existe una instancia, la devolvemos
            // si no, la creamos de forma segura
            return INSTANCIA ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gestor_tareas_db" // Nombre del archivo
                // SQLite en el teléfono
                ).build()
                INSTANCIA = instancia
                instancia
            }
        }
    }
}