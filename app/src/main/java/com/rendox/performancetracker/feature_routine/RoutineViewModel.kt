package com.rendox.performancetracker.feature_routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rendox.performancetracker.data.RoutineDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val routineDataSource: RoutineDataSource
) : ViewModel() {
    val routineList = routineDataSource.getAllRoutines()

    fun insertRoutine(name: String, progress: Double) = viewModelScope.launch {
        routineDataSource.insertRoutine(name = name, progress = progress)
    }

    fun updateRoutineById(id: Long, name: String?, progress: Double?) {
        viewModelScope.launch {
            routineDataSource.updateRoutineById(id = id, name = name, progress = progress)
        }
    }
}

//
fun convertProgressToDouble(progress: Int) =
    progress / 100.0

fun convertProgressToInt(progress: Double) =
    (progress * 100).roundToInt()