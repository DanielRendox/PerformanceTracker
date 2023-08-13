package com.rendox.performancetracker

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class MainViewModel: ViewModel() {
    // TODO replace with database value
    // TODO replace with StateFlow
    private val _routineList = mutableStateListOf<Routine>()
    val routineList
        get() = _routineList

    fun addRoutine(routine: Routine) {
        _routineList.add(routine)
    }
}