package com.example.funfacts

import android.app.Application
import androidx.room.Room
import com.example.funfacts.room.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class TheApp: Application() {

// Uncomment the stuff below to completely delete and recreate the database.
//    override fun onCreate() {
//        super.onCreate()
//        deleteDatabase("myDB")
//    }

    val scope =CoroutineScope(SupervisorJob())

    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "myDB"
        ).build()
    }

    val repository by lazy { Repository(scope, db.factDao()) }
}