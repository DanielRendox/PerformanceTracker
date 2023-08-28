package com.rendox.performancetracker

import android.util.Log
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class MainViewModel(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // TODO replace with database value
    private var _routineList = MutableStateFlow(emptyList<Routine>())
    val routineList
        get() = _routineList

    fun addRoutine(routine: Routine) {
        _routineList.value += routine
    }

    fun editRoutine(index: Int, newValue: Routine) {
        _routineList.value = _routineList.value.toMutableList().also {
            it[index] = newValue
        }.toList()
    }

    // Routine dialog
    private val dialogShown = savedStateHandle.getStateFlow("dialogShown", false)
    private val routineName = savedStateHandle.getStateFlow("routineNameDialogInput", "")
    private val routineProgress = savedStateHandle.getStateFlow("routineProgressDialogInput", "")
    private val isNameWrong = MutableStateFlow(false)
    private val isProgressWrong = MutableStateFlow(false)
    private lateinit var dialogType: MutableStateFlow<DialogType>
    private lateinit var routineIndex: MutableStateFlow<Int>

    private fun checkName() {
        val isNameWrong = routineName.value == ""

        // avoid unnecessary recompositions
        if (isNameWrong != this.isNameWrong.value) {
            this.isNameWrong.value = isNameWrong
        }
    }

    private fun checkProgress() {
        val isProgressWrong =
            try {
                ((routineProgress.value == "") || routineProgress.value.toInt() !in 0..100)
            } catch (ex: NumberFormatException) {
                true
            }

        // avoid unnecessary recompositions
        if (isProgressWrong != this.isProgressWrong.value) {
            this.isProgressWrong.value = isProgressWrong
        }
    }

    val routineDialogState = RoutineDialogState(
        dialogShownProvider = { dialogShown.value },
        routineNameProvider = { routineName.value },
        routineProgressProvider = { routineProgress.value },
        isNameWrongProvider = { isNameWrong.value },
        isProgressWrongProvider = { isProgressWrong.value },
        onShowAddDialog = {
            dialogType = MutableStateFlow(DialogType.Add)
            savedStateHandle["dialogShown"] = true
        },
        onShowEditDialog = { name, progress, index ->
            dialogType = MutableStateFlow(DialogType.Edit)
            savedStateHandle["routineNameDialogInput"] = name
            savedStateHandle["routineProgressDialogInput"] = progress
            routineIndex = MutableStateFlow(index)
            savedStateHandle["dialogShown"] = true
        },
        onCloseDialog = {
            savedStateHandle["dialogShown"] = false
            savedStateHandle["routineNameDialogInput"] = ""
            savedStateHandle["routineProgressDialogInput"] = ""
            isNameWrong.value = false
            isProgressWrong.value = false
        },
        onUpdateName = { newName ->
            savedStateHandle["routineNameDialogInput"] = newName
            checkName()
        },
        onUpdateProgress = { newProgress ->
            savedStateHandle["routineProgressDialogInput"] = newProgress
            checkProgress()
        },
        checkNameProvider = ::checkName,
        checkProgressProvider = ::checkProgress,
        dialogTypeProvider = { dialogType.value },
        routineIndexProvider = { routineIndex.value }
    )
}

/**
TODO The program currently doesn't work properly because the concrete value
is passed to the composable (you should use smth like collectAsState).
But according to [what Gabor says](https://stackoverflow.com/questions/76767202/what-is-the-best-way-to-pass-a-default-initial-value-to-a-jetpack-compose-textfi) this is not that easy. So learn Flows and fix it.
 */
@Stable
class RoutineDialogState(
    private val dialogShownProvider: () -> Boolean,
    private val routineNameProvider: () -> String,
    private val routineProgressProvider: () -> String,
    private val isNameWrongProvider: () -> Boolean,
    private val isProgressWrongProvider: () -> Boolean,
    private val onShowAddDialog: () -> Unit,
    private val onShowEditDialog: (String, String, Int) -> Unit,
    private val onCloseDialog: () -> Unit,
    private val onUpdateName: (String) -> Unit,
    private val onUpdateProgress: (String) -> Unit,
    private val checkNameProvider: () -> Unit,
    private val checkProgressProvider: () -> Unit,
    private val dialogTypeProvider: () -> DialogType,
    private val routineIndexProvider: () -> Int,
) {
    val dialogShown: Boolean
        get() = dialogShownProvider()
    val routineName: String
        get() = routineNameProvider()
    val routineProgress: String
        get() = routineProgressProvider()
    val isNameWrong: Boolean
        get() = isNameWrongProvider()
    val isProgressWrong: Boolean
        get() = isProgressWrongProvider()
    val dialogType: DialogType
        get() = dialogTypeProvider()
    val routineIndex: Int
        get() = routineIndexProvider()

    fun showAddDialog() {
        Log.i("MainViewModel", "set the dialogShown to true")
        onShowAddDialog()
    }

    fun showEditDialog(name: String, progress: String, index: Int) {
        onShowEditDialog(name, progress, index)
    }

    fun closeDialog() {
        onCloseDialog()
    }

    fun updateName(name: String) {
        onUpdateName(name)
    }

    fun updateProgress(progress: String) {
        onUpdateProgress(progress)
    }

    fun availableForSaving(): Boolean {
        checkNameProvider()
        checkProgressProvider()
        return !isNameWrongProvider() && !isProgressWrongProvider()
    }
}