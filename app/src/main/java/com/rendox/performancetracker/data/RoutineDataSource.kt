package com.rendox.performancetracker.data

import comrendoxperformancetracker.Routine
import kotlinx.coroutines.flow.Flow

interface RoutineDataSource {
    fun getAllRoutines(): Flow<List<Routine>>

    suspend fun deleteRoutineById(id: Long)

    suspend fun insertRoutine(name: String, progress: Double, id: Long? = null)

    suspend fun updateRoutineById(id: Long, name: String? = null, progress: Double? = null)
}