package com.codingtho.t0d0.ui.screen.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codingtho.t0d0.data.repository.TaskRepository
import com.codingtho.t0d0.data.repository.model.Task
import com.codingtho.t0d0.data.repository.model.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    data class UiState(
        val isScreenLoading: Boolean = true,
        val title: String = "",
        val titleInputError: Boolean = false,
        val isAddTaskDialogOpen: Boolean = false,
        val isEditTaskDialogOpen: Boolean = false,
        val isDeleteTaskDialogOpen: Boolean = false,
        val taskToEdit: Task? = null,
        val taskToDelete: Task? = null,
        val doneTasks: List<Task> = emptyList(),
        val toDoTasks: List<Task> = emptyList()
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState get() = _uiState.asStateFlow()

    init {
        fetchTasks()
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    doneTasks = taskRepository.getDoneTasks(),
                    toDoTasks = taskRepository.getToDoTasks()
                )
            }
            _uiState.update {
                it.copy(isScreenLoading = false)
            }
        }
    }

    fun onTitleChange(input: String) {
        _uiState.update {
            it.copy(title = input)
        }
    }

    fun isTitleEmpty(): Boolean {
        _uiState.update {
            it.copy(titleInputError = _uiState.value.title.isEmpty() || _uiState.value.title.isBlank())
        }

        return _uiState.value.titleInputError
    }

    private fun resetTitle() {
        _uiState.update {
            it.copy(title = "", titleInputError = false)
        }
    }

    private fun initTaskTitle(task: Task) {
        _uiState.update {
            it.copy(title = task.title)
        }
    }

    fun addTask() {
        viewModelScope.launch {
            taskRepository.insertTask(Task(title = _uiState.value.title))
            fetchTasks()
        }
    }

    fun editTask(task: Task) {
        viewModelScope.launch {
            taskRepository.insertTask(task.copy(title = _uiState.value.title))
            setTaskToEdit(null)
            fetchTasks()
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task.toEntity())
            setTaskToDelete(null)
            fetchTasks()
        }
    }

    fun markAsToDoOrDone(task: Task) {
        viewModelScope.launch {
            taskRepository.insertTask(task.copy(done = !task.done))
            fetchTasks()
        }
    }

    fun setAddTaskDialogOpen(isOpen: Boolean) {
        if (!isOpen) resetTitle()
        _uiState.update {
            it.copy(isAddTaskDialogOpen = isOpen)
        }
    }

    fun setEditTaskDialogOpen(isOpen: Boolean) {
        if (!isOpen) resetTitle()
        _uiState.update {
            it.copy(isEditTaskDialogOpen = isOpen)
        }
    }

    fun setDeleteTaskDialogOpen(isOpen: Boolean) {
        _uiState.update {
            it.copy(isDeleteTaskDialogOpen = isOpen)
        }
    }

    fun setTaskToEdit(task: Task?) {
        if (task != null) initTaskTitle(task)
        _uiState.update {
            it.copy(taskToEdit = task)
        }
    }

    fun setTaskToDelete(task: Task?) {
        _uiState.update {
            it.copy(taskToDelete = task)
        }
    }
}
