package com.udacity.testing.basics.data.source

import androidx.lifecycle.LiveData
import com.udacity.testing.basics.data.Task
import com.udacity.testing.basics.data.TaskResult

/**
 * Main entry point for accessing tasks data.
 */
interface TasksDataSource {

    fun observeTasks(): LiveData<TaskResult<List<Task>>>

    suspend fun getTasks(): TaskResult<List<Task>>

    suspend fun refreshTasks()

    fun observeTask(taskId: String): LiveData<TaskResult<Task>>

    suspend fun getTask(taskId: String): TaskResult<Task>

    suspend fun refreshTask(taskId: String)

    suspend fun saveTask(task: Task)

    suspend fun completeTask(task: Task)

    suspend fun completeTask(taskId: String)

    suspend fun activateTask(task: Task)

    suspend fun activateTask(taskId: String)

    suspend fun clearCompletedTasks()

    suspend fun deleteAllTasks()

    suspend fun deleteTask(taskId: String)
}