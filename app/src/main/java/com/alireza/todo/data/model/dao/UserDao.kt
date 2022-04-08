package com.alireza.todo.data.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alireza.todo.data.model.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun signupUser(user: User)

    @Query("SELECT * FROM user_table where username == :usernameInput")
    fun loginInfo(usernameInput: String): Flow<User?>

    @Delete
    suspend fun deleteUser(user: User)
}