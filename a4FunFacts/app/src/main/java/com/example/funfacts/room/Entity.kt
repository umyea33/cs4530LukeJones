package com.example.funfacts.room

//import android.R
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Entity(tableName = "facts")
data class Fact(
    @PrimaryKey(autoGenerate = true)
    @Transient
    val id: Int=0,
    val text: String,
    val source: String
)