package com.alireza.todo.data.model.model

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithTasks(
    @Embedded val user: User,
    @Relation(parentColumn = "username", entityColumn = "userCreator")
    val tasks: List<Task>
)
