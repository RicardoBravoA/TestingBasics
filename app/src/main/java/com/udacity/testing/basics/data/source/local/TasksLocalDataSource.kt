package com.udacity.testing.basics.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.udacity.testing.basics.data.TaskResult
import com.udacity.testing.basics.data.Task
import com.udacity.testing.basics.data.source.TasksDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Concrete implementation of a data source as a db.
 */
class TasksLocalDataSource internal constructor(
    private val tasksDao: TasksDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : TasksDataSource {

    override fun observeTasks(): LiveData<TaskResult<List<Task>>> {
        return tasksDao.observeTasks().map {
            TaskResult.Success(it)
        }
    }

    override fun observeTask(taskId: String): LiveData<TaskResult<Task>> {
        return tasksDao.observeTaskById(taskId).map {
            TaskResult.Success(it)
        }
    }

    override suspend fun refreshTask(taskId: String) {
        //NO-OP
    }

    override suspend fun refreshTasks() {
        //NO-OP
    }

    override suspend fun getTasks(): TaskResult<List<Task>> = withContext(ioDispatcher) {
        return@withContext try {
            TaskResult.Success(tasksDao.getTasks())
        } catch (e: Exception) {
            TaskResult.Error(e)
        }
    }

    override suspend fun getTask(taskId: String): TaskResult<Task> = withContext(ioDispatcher) {
        try {
            val task = tasksDao.getTaskById(taskId)
            if (task != null) {
                return@withContext TaskResult.Success(task)
            } else {
                return@withContext TaskResult.Error(Exception("Task not found!"))
            }
        } catch (e: Exception) {
            return@withContext TaskResult.Error(e)
        }
    }

    override suspend fun saveTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.insertTask(task)
    }

    override suspend fun completeTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.updateCompleted(task.id, true)
    }

    override suspend fun completeTask(taskId: String) {
        tasksDao.updateCompleted(taskId, true)
    }

    override suspend fun activateTask(task: Task) = withContext(ioDispatcher) {
        tasksDao.updateCompleted(task.id, false)
    }

    override suspend fun activateTask(taskId: String) {
        tasksDao.updateCompleted(taskId, false)
    }

    override suspend fun clearCompletedTasks() = withContext<Unit>(ioDispatcher) {
        tasksDao.deleteCompletedTasks()
    }

    override suspend fun deleteAllTasks() = withContext(ioDispatcher) {
        tasksDao.deleteTasks()
    }

    override suspend fun deleteTask(taskId: String) = withContext<Unit>(ioDispatcher) {
        tasksDao.deleteTaskById(taskId)
    }
}