package com.rendox.performancetracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineDialog(
    onDismissRequest: () -> Unit,
    title: String,
    routineName: String,
    routineProgress: String,
    onNameChange: (String) -> Unit,
    onProgressChange: (String) -> Unit,
    dismissButtonOnClick: () -> Unit,
    confirmButtonOnClick: () -> Unit,
    isNameWrong: Boolean,
    isProgressWrong: Boolean,
    confirmButtonEnabled: () -> Boolean,
) {
    AlertDialog(onDismissRequest = onDismissRequest){
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
                        enabled = confirmButtonEnabled()
                    ) {
                        Text(text = stringResource(R.string.confirm))
                    }
                }
            }
        }
    }
}

enum class DialogType {
    Add,
    Edit;
}