package com.lucacorp.todolucas.network

import com.lucacorp.todolucas.tasklist.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class TasksRepository {
    private val tasksWebService = Api.tasksWebService

    // Ces deux variables encapsulent la même donnée:
    // [_taskList] est modifiable mais privée donc inaccessible à l'extérieur de cette classe
    private val _taskList = MutableStateFlow<List<Task>>(value = emptyList())
    // [taskList] est publique mais non-modifiable:
    // On pourra seulement l'observer (s'y abonner) depuis d'autres classes
    public val taskList: StateFlow<List<Task>> = _taskList.asStateFlow()

    suspend fun refresh() {
        // Call HTTP (opération longue):
        val tasksResponse = tasksWebService.getTasks()
        // À la ligne suivante, on a reçu la réponse de l'API:
        if (tasksResponse.isSuccessful) {
            val fetchedTasks = tasksResponse.body()
            // on modifie la valeur encapsulée, ce qui va notifier ses Observers et donc déclencher leur callback
            if (fetchedTasks != null) _taskList.value = fetchedTasks
        }
    }

    suspend fun createTask(task: Task) {
        val createdTask = tasksWebService.create(task).body()
        if (createdTask != null) {
            val newTask = taskList.value.firstOrNull { it.id == createdTask.id }
            if (newTask != null) {
                _taskList.value = taskList.value + newTask
            }
        }
    }

    suspend fun deleteTask(id: String) {
        val response = tasksWebService.delete(id)
        if (response.isSuccessful) {
            val oldTask = taskList.value.firstOrNull { it.id == id }
            if (oldTask != null) {
                _taskList.value = taskList.value - oldTask
            }
        }
    }

    suspend fun updateTask(task: Task) {
        val updatedTask = tasksWebService.update(task, task.id).body()
        if (updatedTask != null) {
            val oldTask = taskList.value.firstOrNull { it.id == updatedTask.id }
            if (oldTask != null) {
                _taskList.value = taskList.value - oldTask + updatedTask
            }
        }
    }
}