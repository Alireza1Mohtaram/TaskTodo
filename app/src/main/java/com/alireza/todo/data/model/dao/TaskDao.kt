package com.alireza.todo.data.model.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.alireza.todo.data.model.model.Task
import com.alireza.todo.data.model.model.User
import com.alireza.todo.data.model.model.UserWithTasks
import kotlinx.coroutines.flow.Flow


@Dao
interface TaskDao {


//    @Query("SELECT * FROM task_table ORDER BY id ASC")
//    fun getAllTaskUser(): LiveData<List<UserWithTasks>>
//
//    @Query("SELECT * FROM user_table JOIN task_table ON user_table.username = task_table.userCreator ")
//    fun getAllTaskUser2(): Flow<Map<User, List<Task>>>

    @Query("SELECT * FROM task_table where userCreator = :userId")
    fun getAllTaskUser2(userId:String): Flow<List<Task>>

    @Query("SELECT * FROM task_table ")
    fun getAllTaskUserAdmin(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Update
    suspend fun updateTask(task: Task)

    @Query("DELETE FROM task_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM task_table WHERE title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): Flow<List<Task>>
    @Query("SELECT * FROM task_table WHERE id = :id")
    fun findTask(id: Int): Flow<Task>

    @Query("SELECT * FROM task_table WHERE userCreator = :id ORDER BY CASE " +
            "WHEN priority LIKE 'H%' THEN 1 " +
            "WHEN priority LIKE 'M%' THEN 2 " +
            "WHEN priority LIKE 'L%' THEN 3 END")
    fun sortByHighPriority(id: String): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE userCreator = :id ORDER BY CASE " +
            "WHEN priority LIKE 'L%' THEN 1 " +
            "WHEN priority LIKE 'M%' THEN 2 " +
            "WHEN priority LIKE 'H%' THEN 3 END")
    fun sortByLowPriority(id: String): Flow<List<Task>>

     @Delete
    suspend fun deleteTask(task: Task)
}