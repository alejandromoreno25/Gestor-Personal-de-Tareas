package com.example.gestorpersonaldetareas.comunicaciones

import retrofit2.http.GET
import retrofit2.http.Query

interface TareasApiService {
    @GET("todos")
    suspend fun obtenerTareasEjemplo(
        @Query("userId") userId: Int = 1
    ): List<TareaEjemplo>
}