package com.codingtho.t0d0.data.repository.model

import com.codingtho.t0d0.data.local.entity.TaskEntity

data class Task(
    val id: Int = 0,
    val title: String,
    val done: Boolean = false
)

fun Task.toEntity() = TaskEntity(id = id, title = title, done = done)
