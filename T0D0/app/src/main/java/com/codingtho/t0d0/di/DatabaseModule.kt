package com.codingtho.t0d0.di

import android.app.Application
import androidx.room.Room
import com.codingtho.t0d0.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(app: Application): AppDatabase =
        Room.databaseBuilder(app, AppDatabase::class.java, "task_database")
            .build()

    @Singleton
    @Provides
    fun provideTaskDao(db: AppDatabase) = db.getTaskDao()
}
