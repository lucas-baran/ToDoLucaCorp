package com.lucacorp.todolucas.tasklist

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
}