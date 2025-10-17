package com.example.roomdemo

import android.app.Application
import androidx.room.Room
import com.example.roomdemo.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TodoApp: Application() {
val scope =CoroutineScope(SupervisorJob())
    val db by lazy {
        Room.databaseBuilder(
                applicationContext,
            AppDatabase::class.java,
            "myDB"
                ).build()
    }

    val repository by lazy { Repository(scope, db.taskDao()) }

}