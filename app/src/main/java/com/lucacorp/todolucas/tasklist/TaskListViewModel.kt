package com.lucacorp.todolucas.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lucacorp.todolucas.network.TasksRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Le ViewModel met à jour la liste de task qui est un StateFlow
class TaskListViewModel: ViewModel() {
    private val repository = TasksRepository()
    private val _taskList = MutableStateFlow<List<Task>>(value = emptyList())
    public val taskList: StateFlow<List<Task>> = _taskList

    fun refresh() {
        viewModelScope.launch {
            val taskList = repository.loadTasks()
            if (taskList != null) {
                _taskList.value = taskList;
            }
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.deleteTask(task)
            refresh()
        }
    }

    fun createTask(task: Task) {
        viewModelScope.launch {
            repository.createTask(task)
            refresh()
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.updateTask(task)
            refresh()
        }
    }
}