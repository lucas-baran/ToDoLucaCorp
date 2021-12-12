package com.lucacorp.todolucas.network

import com.lucacorp.todolucas.tasklist.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    suspend fun loadTasks(): List<Task>? {
        val response = tasksWebService.getTasks()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun createTask(task: Task) {
        tasksWebService.create(task)
    }

    suspend fun deleteTask(task: Task) {
        tasksWebService.delete(task.id)
    }

    suspend fun updateTask(task: Task) {
        tasksWebService.update(task, task.id)
    }
}