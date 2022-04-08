package com.alireza.todo.ui.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.alireza.todo.data.model.DatabaseTask
import com.alireza.todo.data.model.model.Opreation
import com.alireza.todo.data.model.model.Task
import com.alireza.todo.data.model.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel (application: Application) : AndroidViewModel(application) {

        private val taskDao = DatabaseTask.getDatabase(application).taskDao()
        private val repository: TaskRepository = TaskRepository(taskDao)
        val opreation = MutableLiveData<Opreation>()



    fun searchDatabase(searchQuery: String): LiveData<List<Task>> {
        return repository.searchDatabase(searchQuery).asLiveData()
    }

     fun getAllTask(userid:String): LiveData<List<Task>> {
         return repository.getAllTask(userid).asLiveData()
    }
    fun insertData(task: Task){
        viewModelScope.launch {
            repository.insertTask(task)
        }
    }
    fun deleteTask(task: Task){
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }
    fun updateTask(task: Task){
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }
    fun finTask(taskId:Int): LiveData<Task> {
        return repository.findTask(taskId).asLiveData()
    }
    fun sortByHighPriority(username:String) : LiveData<List<Task>> {
        return taskDao.sortByHighPriority(username).asLiveData()
    }
    fun sortByLowPriority(username: String) : LiveData<List<Task>> {
        return taskDao.sortByLowPriority(username).asLiveData()
    }

}