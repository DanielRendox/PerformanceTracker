package com.rendox.performancetracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class RoutineDialogState(
    routineName: String = "",
    routineProgress: String = "",
    isNameWrong: Boolean = false,
    isProgressWrong: Boolean = false,
) {
    var routineName by mutableStateOf(routineName)
        private set
    var routineProgress by mutableStateOf(routineProgress)
        private set
    var isNameWrong by mutableStateOf(isNameWrong)
        private set
    var isProgressWrong by mutableStateOf(isProgressWrong)
        private set

    private fun checkProgress(progress: String) {
        isProgressWrong = try {
            ((progress == "") || progress.toInt() !in 0..100)
        } catch (ex: NumberFormatException) {
            true
        }
    }

    private fun checkName(name: String) {
        isNameWrong = name == ""
    }

    fun updateName(name: String) {
        routineName = name
        checkName(routineName)
    }

    fun updateProgress(progress: String) {
        routineProgress = progress
        checkProgress(routineProgress)
    }

    fun availableForSaving(): Boolean {
        checkName(routineName)
        checkProgress(routineProgress)
        return !isNameWrong && !isProgressWrong
    }

    companion object {
        val Saver: Saver<RoutineDialogState, *> = listSaver(
            save = {
                listOf(
                    it.routineName,
                    it.routineProgress,
                    it.isNameWrong,
                    it.isProgressWrong,
                )
            },
            restore = {
                RoutineDialogState(
                    routineName = it[0] as String,
                    routineProgress = it[1] as String,
                    isNameWrong = it[2] as Boolean,
                    isProgressWrong = it[3] as Boolean,
                )
            }
        )
    }
}


@Composable
fun rememberRoutineDialogState() =
    rememberSaveable(saver = RoutineDialogState.Saver) {
        RoutineDialogState()
    }

@Composable
fun RoutineDialogContent(
    title: String,
    routineName: String,
    routineProgress: String,
    onNameChange: (String) -> Unit,
    onProgressChange: (String) -> Unit,
    dismissButtonOnClick: () -> Unit,
    confirmButtonOnClick: () -> Unit,
    isNameWrong: Boolean,
    isProgressWrong: Boolean,
) {
    Surface(
        modifier = Modifier
            .wrapContentWidth()
            .wrapContentHeight(),
        shape = MaterialTheme.shapes.large,
        tonalElevation = AlertDialogDefaults.TonalElevation,
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.CenterHorizontally),
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(bottom = 4.dp),
                singleLine = true,
                value = routineName,
                onValueChange = onNameChange,
                placeholder = { Text(text = stringResource(R.string.progress_bars_dialog_text_field_name_placeholder)) },
                label = { Text(text = stringResource(R.string.progress_bars_dialog_name_text_field_label)) },
                isError = isNameWrong,
                supportingText = {
                    if (isNameWrong) {
                        Text(text = stringResource(R.string.progress_bars_wrong_name_error_description))
                    }
                }
            )
            OutlinedTextField(
                modifier = Modifier
                    .padding(bottom = 24.dp),
                singleLine = true,
                value = routineProgress,
                onValueChange = onProgressChange,
                label = { Text(text = stringResource(R.string.progress_bars_dialog_progress_text_field_label)) },
                suffix = { Text(text = stringResource(R.string.progress_bars_dialog_progress_text_field_suffix)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isProgressWrong,
                supportingText = {
                    if (isProgressWrong) {
                        Text(text = stringResource(R.string.progress_bars_wrong_progress_error_description))
                    }
                }
            )
            Row(modifier = Modifier.align(Alignment.End)) {
                TextButton(
                    onClick = dismissButtonOnClick,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text(text = stringResource(android.R.string.cancel))
                }
                TextButton(
                    onClick = confirmButtonOnClick,
                    enabled = !isNameWrong && !isProgressWrong
                ) {
                    Text(text = stringResource(R.string.confirm))
                }
            }
        }
    }
}