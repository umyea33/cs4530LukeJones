package com.example.mvvmdemo

import com.example.mvvmdemo.room.CourseDao
import com.example.mvvmdemo.room.CourseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class Repository (val scope: CoroutineScope, private val dao: CourseDao) {
    val allCourses: Flow<List<CourseEntity?>> = dao.getAllCourses()

    fun addCourse(dep: String, number: String, location: String) {
        scope.launch {
            val courseObj = CourseEntity(0, dep, number, location, 0)
            dao.insertCourse(courseObj)
        }
    }

    fun updateCourse(id: Int, dep: String, number: String, location: String) {
        scope.launch {
            val courseObj = CourseEntity(id, dep, number, location, 1)
            dao.insertCourse(courseObj)
        }
    }

    fun deleteCourse(id: Int) {
        scope.launch {
            dao.deleteCourse(id)
        }
    }

    fun updateMode(id: Int, mode: Int) {
        scope.launch {
            dao.updateMode(id, mode)
        }
    }
}