package com.example.roomdemo

import com.example.roomdemo.room.TaskDao
import com.example.roomdemo.room.TaskEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class Repository (val scope: CoroutineScope, private val dao: TaskDao) {

    val allTasks: Flow<List<TaskEntity?>> = dao.getAllTasks()

    fun addTask(task: String) {
        scope.launch {
            delay(1000) // simulates network delay
            val taskObj = TaskEntity(task)
            dao.insertTask(taskObj)
        }
    }
}