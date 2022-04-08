package com.alireza.todo.data.model.utils

import androidx.room.TypeConverter
import com.alireza.todo.data.model.model.Priority
import com.alireza.todo.data.model.model.State
import java.util.*

class Converter {
    @TypeConverter
    fun fromPriority(priority: Priority):String{
        return priority.name
    }
    @TypeConverter
    fun toPriority(stringPriority: String): Priority {
        return Priority.valueOf(stringPriority)
    }
    @TypeConverter
    fun fromState(state: State):String{
        return state.name
    }
    @TypeConverter
    fun toState(stringState: String): State {
        return State.valueOf(stringState)
    }
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

}