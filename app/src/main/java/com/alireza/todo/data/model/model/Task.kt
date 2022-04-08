package com.alireza.todo.data.model.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity("task_table")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val title:String,
    val description : String,
    val dateTime : Date,
    val priority: Priority,
    val state: State,
    val userCreator:String,
    val imgUri : String? = null
    )