package com.codingtho.t0d0.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.codingtho.t0d0.data.local.entity.TaskEntity

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Query("SELECT * FROM task_table WHERE done = 0")
    suspend fun getNotDoneTasks(): List<TaskEntity>

    @Query("SELECT * FROM task_table WHERE done = 1")
    suspend fun getDoneTasks(): List<TaskEntity>

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}
