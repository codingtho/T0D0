package com.codingtho.t0d0.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.codingtho.t0d0.data.local.dao.TaskDao
import com.codingtho.t0d0.data.local.entity.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}
