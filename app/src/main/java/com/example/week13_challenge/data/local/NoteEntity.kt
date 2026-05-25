package com.example.week13_challenge.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.week13_challenge.domain.Priority

@Entity(tableName = "notes")
@TypeConverters(Converters::class)

data class NoteEntity(
    @PrimaryKey(autoGenerate = true) val id:Long = 0L,
    val title:String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val completed: Boolean = false,
    val priority: Priority = Priority.None
)