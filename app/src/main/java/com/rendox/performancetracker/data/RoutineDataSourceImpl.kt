package com.rendox.performancetracker.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.rendox.performancetracker.Database
import comrendoxperformancetracker.Routine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class RoutineDataSourceImpl(
    db: Database
) : RoutineDataSource {

    private val queries = db.routineQueries

    override fun getAllRoutines(): Flow<List<Routine>> {
        return queries.getAllRoutines().asFlow().mapToList(Dispatchers.Default)
    }

    override suspend fun deleteRoutineById(id: Long): Unit = withContext(Dispatchers.IO) {
        queries.deleteRoutineById(id)
    }

    override suspend fun insertRoutine(name: String, progress: Double, id: Long?) =
        withContext(Dispatchers.IO) {
            queries.insertRoutine(id, name, progress)
        }

    override suspend fun updateRoutineById(id: Long, name: String?, progress: Double?) =
        withContext(Dispatchers.IO) {
            name?.let { queries.updateRoutineName(name = it, id = id) } ?: Unit
            progress?.let { queries.updateRoutineProgress(progress = it, id = id) } ?: Unit
        }
}