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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressBarsScreen() {

    var dialogShown by rememberSaveable { mutableStateOf(false) }
    var dialogTitle by rememberSaveable { (mutableStateOf("")) }

    val viewModel = viewModel<MainViewModel>()
    val routineList = viewModel.routineList

    if (dialogShown) {
        val dialogState = rememberRoutineDialogState()
        AlertDialog(onDismissRequest = { dialogShown = false }) {
            RoutineDialogContent(
                title = dialogTitle,
                routineName = dialogState.routineName,
                routineProgress = dialogState.routineProgress,
                onNameChange = {
                    dialogState.updateName(it)
                },
                onProgressChange = {
                    dialogState.updateProgress(it)
                },
                dismissButtonOnClick = {
                    dialogShown = false
                },
                confirmButtonOnClick = {
                    if (dialogState.availableForSaving()) {
                        dialogShown = false
                        viewModel.addRoutine(
                            Routine(
                                title = dialogState.routineName,
                                progress = (dialogState.routineProgress.toInt()) / 100f
                            )
                        )
                    }
                },
                isNameWrong = dialogState.isNameWrong,
                isProgressWrong = dialogState.isProgressWrong,
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
                dialogTitle = title
                dialogShown = true
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.progress_bars_fab_icon_description)
                )
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