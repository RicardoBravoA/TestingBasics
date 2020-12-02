package com.udacity.testing.basics.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.udacity.testing.basics.data.Task

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TestDatabase : RoomDatabase() {

    abstract fun taskDao(): TasksDao
}