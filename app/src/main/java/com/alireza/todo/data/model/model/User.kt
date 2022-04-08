package com.alireza.todo.data.model.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("user_table")
data class User(
    @PrimaryKey
    val username:String,
    var password:String
)