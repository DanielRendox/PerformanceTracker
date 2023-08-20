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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.roundToInt

@Composable
fun ProgressBarsScreen() {

    val dialogState = rememberRoutineDialogState()
    val viewModel = viewModel<MainViewModel>()

    if (dialogState.dialogShown) {
        RoutineDialog(
            onDismissRequest = { dialogState.closeDialog() },
            title = when (dialogState.dialogType) {
                DialogType.Add -> stringResource(R.string.progress_bars_dialog_title_add_routine)
                DialogType.Edit -> stringResource(R.string.progress_bars_dialog_title_edit_routine)
            },
            routineName = dialogState.routineName,
            routineProgress = dialogState.routineProgress,
            onNameChange = {
                dialogState.updateName(it)
            },
            onProgressChange = {
                dialogState.updateProgress(it)
            },
            dismissButtonOnClick = {
                dialogState.closeDialog()
            },
            confirmButtonOnClick = {
                if (dialogState.availableForSaving()) {
                    when (dialogState.dialogType) {
                        DialogType.Add -> {
                            viewModel.addRoutine(
                                Routine(
                                    name = dialogState.routineName,
                                    progress = Routine.convertProgressToFloat(
                                        dialogState.routineProgress.toInt()
                                    )
                                )
                            )
                        }

                        DialogType.Edit -> {
                            viewModel.editRoutine(
                                index = dialogState.routineIndex,
                                newValue = Routine(
                                    name = dialogState.routineName,
                                    progress = Routine.convertProgressToFloat(
                                        dialogState.routineProgress.toInt()
                                    )
                                )
                            )
                        }
                    }
                    dialogState.closeDialog()
                }
            },
            isNameWrong = dialogState.isNameWrong,
            isProgressWrong = dialogState.isProgressWrong,
            confirmButtonEnabled = { !dialogState.isNameWrong && !dialogState.isProgressWrong }
        )
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
            FloatingActionButton(onClick = {
                dialogState.showAddDialog()
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.progress_bars_fab_icon_description)
                )
            }
        }
    ) { paddingValues ->
        val routineListState = viewModel.routineList.collectAsState()
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = paddingValues,
        ) {
            items(routineListState.value) { routine ->
                ProgressBarComponent(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp),
                    name = routine.name,
                    progress = routine.progress,
                    editButtonOnClick = { name, progress ->
                        dialogState.showEditDialog(
                            routineName = name,
                            routineProgress = Routine.convertProgressToInt(progress).toString(),
                            routineIndex = routineListState.value.indexOf(routine)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun ProgressBarComponent(
    modifier: Modifier,
    name: String,
    progress: Float,
    editButtonOnClick: (title: String, progress: Float) -> Unit,
) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier
                .weight(2f)
                .padding(end = 24.dp),
            text = name,
            style = MaterialTheme.typography.titleLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
        Column(
            modifier = Modifier
                .weight(4f)
        ) {
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
        IconButton(
            onClick = { editButtonOnClick(name, progress) },
            modifier = Modifier
                .weight(1f)
                .padding(start = 24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = stringResource(R.string.progress_bars_progress_component_edit_button_description),
            )
        }
    }
}

@Preview(
    widthDp = 400,
    heightDp = 800,
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun ProgressBarsScreenPreview() {
    ProgressBarsScreen()
}

@Preview(
    showBackground = true,
    widthDp = 600,
)
@Composable
fun ProgressBarComponentPreview() {
    ProgressBarComponent(
        modifier = Modifier.padding(24.dp),
        name = "Title",
        progress = 0.4f,
        editButtonOnClick = { _, _ -> }
    )
}