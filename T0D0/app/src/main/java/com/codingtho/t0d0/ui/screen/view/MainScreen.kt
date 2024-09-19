package com.codingtho.t0d0.ui.screen.view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.codingtho.t0d0.data.repository.model.Task
import com.codingtho.t0d0.ui.screen.viewModel.MainScreenViewModel

@Composable
fun MainScreen(viewModel: MainScreenViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar() },
        floatingActionButton = { AddTaskButton(viewModel) }
    ) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        if (uiState.isScreenLoading) {
            LoadingIndicator(modifier)
        } else {
            AnimatedVisibility(
                visible = uiState.toDoTasks.isEmpty() && uiState.doneTasks.isEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                NoTasksFound(modifier)
            }

            AnimatedVisibility(
                visible = uiState.toDoTasks.isNotEmpty() || uiState.doneTasks.isNotEmpty(),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                TasksFound(viewModel, modifier)
            }
        }
    }
}

@Composable
private fun LoadingIndicator(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        CircularProgressIndicator(
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar() {
    TopAppBar(
        title = {
            Text(
                text = "T0D0",
                fontSize = MaterialTheme.typography.displaySmall.fontSize,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    )
}

@Composable
private fun AddTaskButton(viewModel: MainScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    LargeFloatingActionButton(
        onClick = { viewModel.setAddTaskDialogOpen(true) }
    ) {
        Text(
            text = "+1",
            fontSize = MaterialTheme.typography.displayMedium.fontSize,
            fontWeight = FontWeight.Bold
        )
    }

    if (uiState.isAddTaskDialogOpen) {
        AddTaskDialog(viewModel)
    }
}

@Composable
private fun AddTaskDialog(viewModel: MainScreenViewModel) {
    TaskDialog(
        icon = "+1",
        title = "> ADD TASK",
        textButton = "Add",
        body = { TaskTextField(viewModel) },
        onDismiss = { viewModel.setAddTaskDialogOpen(false) },
        confirmButton = {
            if (!viewModel.isTitleEmpty()) {
                viewModel.addTask()
                viewModel.setAddTaskDialogOpen(false)
            }
        },
        dismissButton = { viewModel.setAddTaskDialogOpen(false) }
    )
}

@Composable
private fun TaskDialog(
    icon: String,
    title: String,
    textButton: String,
    body: @Composable () -> Unit,
    onDismiss: () -> Unit,
    confirmButton: () -> Unit,
    dismissButton: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(
                onClick = { confirmButton() }
            ) {
                Text(textButton)
            }
        },
        dismissButton = {
            TextButton(
                onClick = { dismissButton() }
            ) {
                Text("Cancel")
            }
        },
        icon = {
            Text(
                text = icon,
                fontSize = MaterialTheme.typography.displayMedium.fontSize,
                fontWeight = FontWeight.Bold
            )
        },
        title = { Text(title) },
        text = { body() }
    )
}

@Composable
private fun TaskTextField(viewModel: MainScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    OutlinedTextField(
        value = uiState.title,
        onValueChange = { viewModel.onTitleChange(it) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        label = { Text("Title") },
        placeholder = { if (uiState.titleInputError) Text(text = "Required Field") },
        isError = uiState.titleInputError,
        singleLine = true
    )
}

@Composable
private fun NoTasksFound(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = "No tasks found",
            modifier = Modifier.align(Alignment.Center),
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        )
    }
}

@Composable
private fun TasksFound(viewModel: MainScreenViewModel, modifier: Modifier) {
    val uiState by viewModel.uiState.collectAsState()

    LazyColumn(
        modifier = modifier
    ) {
        item {
            AnimatedVisibility(visible = uiState.toDoTasks.isNotEmpty()) {
                TasksList(viewModel, uiState.toDoTasks, "To Do")
            }
        }

        item {
            AnimatedVisibility(visible = uiState.doneTasks.isNotEmpty()) {
                TasksList(viewModel, uiState.doneTasks, "Done")
            }
        }
    }
}

@Composable
private fun TasksList(viewModel: MainScreenViewModel, tasks: List<Task>, title: String) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(animationSpec = tween(300))
    ) {
        Column {
            Text(
                text = title,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )

            Column {
                tasks.forEach {
                    TaskItem(viewModel, it)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TaskItem(viewModel: MainScreenViewModel, task: Task) {
    val uiState by viewModel.uiState.collectAsState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(8.dp))
            .combinedClickable(
                onClick = {
                    viewModel.setTaskToEdit(task)
                    viewModel.setEditTaskDialogOpen(true)
                },
                onLongClick = {
                    viewModel.setTaskToDelete(task)
                    viewModel.setDeleteTaskDialogOpen(true)
                }
            )
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        CheckButton(viewModel, task)
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = task.title,
            fontSize = MaterialTheme.typography.bodyLarge.fontSize
        )
    }

    if (uiState.isEditTaskDialogOpen && uiState.taskToEdit == task) {
        EditTaskDialog(viewModel)
    }

    if (uiState.isDeleteTaskDialogOpen && uiState.taskToDelete == task) {
        DeleteTaskDialog(viewModel)
    }
}

@Composable
private fun EditTaskDialog(viewModel: MainScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    TaskDialog(
        icon = "1",
        title = "> EDIT TASK",
        textButton = "Save",
        body = {
            TaskTextField(viewModel)
        },
        onDismiss = { viewModel.setEditTaskDialogOpen(false) },
        confirmButton = {
            if (!viewModel.isTitleEmpty()) {
                viewModel.editTask(uiState.taskToEdit!!)
                viewModel.setEditTaskDialogOpen(false)
            }
        },
        dismissButton = { viewModel.setEditTaskDialogOpen(false) }
    )
}

@Composable
private fun DeleteTaskDialog(viewModel: MainScreenViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    TaskDialog(
        icon = "-1",
        title = "> DELETE TASK",
        textButton = "Delete",
        body = { Text(text = "Are you sure you want to delete this task?") },
        onDismiss = { viewModel.setDeleteTaskDialogOpen(false) },
        confirmButton = {
            viewModel.deleteTask(uiState.taskToDelete!!)
            viewModel.setDeleteTaskDialogOpen(false)
        },
        dismissButton = { viewModel.setDeleteTaskDialogOpen(false) }
    )
}

@Composable
private fun CheckButton(viewModel: MainScreenViewModel, task: Task) {
    Box(
        modifier = Modifier
            .size(32.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = MaterialTheme.colorScheme.surfaceDim)
            .clickable {
                if (task.done) viewModel.markAsToDoOrDone(task)
                else viewModel.markAsToDoOrDone(task)
            }
    ) {
        Text(
            text = if (task.done) "1" else "0",
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            fontSize = MaterialTheme.typography.bodyMedium.fontSize,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}
