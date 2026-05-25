package com.example.week13_challenge.data.local

import androidx.room.TypeConverter
import com.example.week13_challenge.domain.Priority

class Converters {
    @TypeConverter
    fun fromPriority(priority: Priority):String = priority.name

    @TypeConverter
    fun toPriority(value:String):Priority = Priority.valueOf(value)
}