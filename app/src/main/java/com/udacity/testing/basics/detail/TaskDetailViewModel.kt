package com.udacity.testing.basics.detail

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.*
import com.udacity.testing.basics.R
import com.udacity.testing.basics.data.Task
import com.udacity.testing.basics.data.TaskResult
import com.udacity.testing.basics.data.source.DefaultTasksRepository
import com.udacity.testing.basics.util.SingleEvent
import kotlinx.coroutines.launch

/**
 * ViewModel for the Details screen.
 */
class TaskDetailViewModel(application: Application) : AndroidViewModel(application) {

    // Note, for testing and architecture purposes, it's bad practice to construct the repository
    // here. We'll show you how to fix this during the codelab
    private val tasksRepository = DefaultTasksRepository.getRepository(application)

    private val _taskId = MutableLiveData<String>()

    private val _task = _taskId.switchMap { taskId ->
        tasksRepository.observeTask(taskId).map { computeResult(it) }
    }
    val task: LiveData<Task?> = _task

    val isDataAvailable: LiveData<Boolean> = _task.map { it != null }

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _editTaskEvent = MutableLiveData<SingleEvent<Unit>>()
    val editTaskEvent: LiveData<SingleEvent<Unit>> = _editTaskEvent

    private val _deleteTaskEvent = MutableLiveData<SingleEvent<Unit>>()
    val deleteTaskEvent: LiveData<SingleEvent<Unit>> = _deleteTaskEvent

    private val _snackbarText = MutableLiveData<SingleEvent<Int>>()
    val snackbarText: LiveData<SingleEvent<Int>> = _snackbarText

    // This LiveData depends on another so we can use a transformation.
    val completed: LiveData<Boolean> = _task.map { input: Task? ->
        input?.isCompleted ?: false
    }

    fun deleteTask() = viewModelScope.launch {
        _taskId.value?.let {
            tasksRepository.deleteTask(it)
            _deleteTaskEvent.value = SingleEvent(Unit)
        }
    }

    fun editTask() {
        _editTaskEvent.value = SingleEvent(Unit)
    }

    fun setCompleted(completed: Boolean) = viewModelScope.launch {
        val task = _task.value ?: return@launch
        if (completed) {
            tasksRepository.completeTask(task)
            showSnackbarMessage(R.string.task_marked_complete)
        } else {
            tasksRepository.activateTask(task)
            showSnackbarMessage(R.string.task_marked_active)
        }
    }

    fun start(taskId: String?) {
        // If we're already loading or already loaded, return (might be a config change)
        if (_dataLoading.value == true || taskId == _taskId.value) {
            return
        }
        // Trigger the load
        _taskId.value = taskId
    }

    private fun computeResult(taskResult: TaskResult<Task>): Task? {
        return if (taskResult is TaskResult.Success) {
            taskResult.data
        } else {
            showSnackbarMessage(R.string.loading_tasks_error)
            null
        }
    }


    fun refresh() {
        // Refresh the repository and the task will be updated automatically.
        _task.value?.let {
            _dataLoading.value = true
            viewModelScope.launch {
                tasksRepository.refreshTask(it.id)
                _dataLoading.value = false
            }
        }
    }

    private fun showSnackbarMessage(@StringRes message: Int) {
        _snackbarText.value = SingleEvent(message)
    }
}