package com.rendox.performancetracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressBarsScreen() {

    // TODO Make a state holder
    var dialogShown by rememberSaveable { mutableStateOf(false) }
    var dialogTitle by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var progress by rememberSaveable { mutableStateOf("") }
    var isNameWrong by rememberSaveable { mutableStateOf(false) }
    var isProgressWrong by rememberSaveable { mutableStateOf(false) }

    fun resetToDefaultValues() {
        name = ""
        progress = ""
        dialogTitle = ""
        isNameWrong = false
        isProgressWrong = false
    }

    fun checkName() {
        isNameWrong = (name == "")
    }

    fun checkProgress() {
        isProgressWrong = try {
            ((progress == "") || progress.toInt() !in 0..100)
        } catch (ex: NumberFormatException) {
            true
        }
    }

    val viewModel = viewModel<MainViewModel>()
    val routineList = viewModel.routineList

    // TODO replace with a view
    if (dialogShown) {
        AlertDialog(onDismissRequest = { dialogShown = false }) {
            RoutineDialogContent(
                title = dialogTitle,
                routineName = name,
                routineProgress = progress,
                onNameChange = {
                    name = it
                    checkName()
                },
                onProgressChange = {
                    progress = it
                    checkProgress()
                },
                dismissButtonOnClick = {
                    dialogShown = false
                    resetToDefaultValues()
                },
                confirmButtonOnClick = {
                    checkName()
                    checkProgress()
                    if (!isNameWrong && !isProgressWrong) {
                        dialogShown = false
                        viewModel.addRoutine(
                            Routine(
                                title = name,
                                progress = (progress.toInt()) / 100f
                            )
                        )
                        resetToDefaultValues()
                    }
                },
                isNameWrong = isNameWrong,
                isProgressWrong = isProgressWrong,
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(64.dp)
            ) {
                Text(
                    text = stringResource(R.string.progress_bars_screen_name),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.Center),
                )
            }
        },
        floatingActionButton = {
            val title = stringResource(R.string.progress_bars_dialog_title_add_routine)
            FloatingActionButton(onClick = {
                dialogShown = true
                dialogTitle = title
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(R.string.progress_bars_fab_icon_description))
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = paddingValues,
        ) {
            items(routineList) { routine ->
                ProgressBarComponent(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    title = routine.title,
                    progress = routine.progress,
                )
            }
        }
    }
}

@Composable
fun ProgressBarComponent(
    modifier: Modifier,
    title: String,
    progress: Float
) {
    Row(
        modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        Text(
            modifier = Modifier
                .width(160.dp)
                .padding(end = 16.dp),
            text = title,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Column(modifier = Modifier.padding(bottom = 8.dp)) {
            val progressInt = (progress * 100).roundToInt()
            Text(
                text = stringResource(R.string.progress_bars_routine_progress_text, progressInt),
                style = MaterialTheme.typography.labelMedium,
            )
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .fillMaxWidth(),
                strokeCap = StrokeCap.Round,
            )
        }
        // TODO add a "modify" IconButton
    }
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

@Preview(
    widthDp = 400,
    heightDp = 800,
    showBackground = true,
    showSystemUi = true,
    backgroundColor = 0xFFFFFFFF,
)
@Composable
fun ProgressBarsScreenPreview() {
    ProgressBarsScreen()
}

@Preview
@Composable
fun ProgressBarComponentPreview() {
    ProgressBarComponent(
        modifier = Modifier.padding(24.dp),
        title = "Title",
        progress = 0.4f
    )
}