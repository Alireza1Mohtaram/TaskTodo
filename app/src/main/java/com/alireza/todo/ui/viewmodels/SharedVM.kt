package com.alireza.todo.ui.viewmodels


import android.app.Application
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.*
import com.alireza.todo.R
import com.alireza.todo.data.model.DatabaseTask
import com.alireza.todo.data.model.model.Priority
import com.alireza.todo.data.model.model.Task
import com.alireza.todo.data.model.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedVM(application: Application) : AndroidViewModel(application) {
    private val taskDao = DatabaseTask.getDatabase(application).taskDao()
    var allData: LiveData<List<Task>> = MutableLiveData(emptyList())
    private val repository: TaskRepository = TaskRepository(taskDao)

    fun getAllTask(userid:String) {
        allData =  repository.getAllTask(userid).asLiveData()
    }
    fun getAllTaskAdmin() : LiveData<List<Task>>{
        return repository.getAllTaskAdmin().asLiveData()
    }
    fun deleteAllTask() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTask()
        }
    }

}
