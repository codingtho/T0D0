package com.codingtho.t0d0.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.codingtho.t0d0.data.repository.model.Task

@Entity(tableName = "task_table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id") val id: Int = 0,
    @ColumnInfo("title") val title: String,
    @ColumnInfo("done") val done: Boolean = false
)

fun TaskEntity.toItem() = Task(id = id, title = title, done = done)
