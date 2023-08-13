package com.rendox.performancetracker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel : ViewModel() {
    // TODO replace with database value
    private var _routineList = MutableStateFlow(emptyList<Routine>())
    val routineList
        get() = _routineList

    fun addRoutine(routine: Routine) {
        _routineList.value += routine
    }
}