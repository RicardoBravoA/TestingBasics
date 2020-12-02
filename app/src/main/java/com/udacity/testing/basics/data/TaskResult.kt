package com.udacity.testing.basics.data

import java.lang.Exception

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class TaskResult<out R> {

    data class Success<out T>(val data: T) : TaskResult<T>()
    data class Error(val exception: Exception?) : TaskResult<Nothing>()
    object Loading : TaskResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

/**
 * `true` if [TaskResult] is of type [Success] & holds non-null [Success.data].
 */
val TaskResult<*>.succeeded
    get() = this is TaskResult.Success && data != null