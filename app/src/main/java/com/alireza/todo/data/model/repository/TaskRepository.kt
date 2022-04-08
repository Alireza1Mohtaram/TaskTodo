package com.alireza.todo.data.model.repository

import com.alireza.todo.data.model.dao.TaskDao
import com.alireza.todo.data.model.model.Task
import kotlinx.coroutines.flow.Flow

class TaskRepository (private val taskDao: TaskDao) {

    fun searchDatabase(searchQuery: String): Flow<List<Task>> {
        return taskDao.searchDatabase(searchQuery)
    }
    suspend fun deleteAllTask(){
        taskDao.deleteAll()
    }
    fun getAllTask(userid:String): Flow<List<Task>> {
        return taskDao.getAllTaskUser2(userid)
    }
    fun getAllTaskAdmin(): Flow<List<Task>> {
        return taskDao.getAllTaskUserAdmin()
    }
    suspend fun insertTask(task: Task){
        taskDao.insertTask(task)
    }
    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }
     fun findTask(taskId: Int) :Flow<Task>{
        return taskDao.findTask(taskId)
    }
    fun sortByHighPriority(username:String) : Flow<List<Task>>{
        return taskDao.sortByHighPriority(username)
    }
    fun sortByLowPriority(username:String) : Flow<List<Task>>{
        return taskDao.sortByLowPriority(username)
    }

   suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }
}