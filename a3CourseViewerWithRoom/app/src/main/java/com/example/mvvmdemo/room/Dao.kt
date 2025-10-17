package com.example.mvvmdemo.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse (course: CourseEntity)

    @Query("select * from courses order by id desc")
    fun getAllCourses(): Flow<List<CourseEntity>>

    @Query("delete from courses where id = :id")
    suspend fun deleteCourse(id: Int)

    @Query("update courses set mode = :mode where id = :id")
    suspend fun updateMode(id: Int, mode: Int)
}