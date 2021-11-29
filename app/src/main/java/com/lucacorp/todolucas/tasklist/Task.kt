package com.lucacorp.todolucas.tasklist

import java.io.Serializable

data class Task(val id: String, val title: String, val description: String = "Task description") : Serializable
