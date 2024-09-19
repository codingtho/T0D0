package com.codingtho.t0d0.data.repository

import com.codingtho.t0d0.data.local.dao.TaskDao
import com.codingtho.t0d0.data.local.entity.TaskEntity
import com.codingtho.t0d0.data.local.entity.toItem
import com.codingtho.t0d0.data.repository.model.Task
import com.codingtho.t0d0.data.repository.model.toEntity
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    suspend fun insertTask(task: Task) {
        taskDao.insertTask(task.toEntity())
    }

    suspend fun getToDoTasks(): List<Task> {
        return try {
            taskDao.getNotDoneTasks().map { it.toItem() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getDoneTasks(): List<Task> {
        return try {
            taskDao.getDoneTasks().map { it.toItem() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun deleteTask(task: TaskEntity) {
        taskDao.deleteTask(task)
    }
}
