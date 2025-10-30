package com.example.funfacts.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFact (fact: Fact)

    @Query("select * from facts order by id desc")
    fun getAllFacts(): Flow<List<Fact>>

    @Query("delete from facts where id = :id")
    suspend fun deleteFact(id: Int)
}