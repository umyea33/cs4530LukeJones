package com.example.mvvmdemo.room

//import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "courses")
data class CourseEntity(@PrimaryKey(autoGenerate = true) val id: Int=0,
                        val dep: String, val num: String, val loc: String, val mode: Int)