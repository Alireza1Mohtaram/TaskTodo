package com.alireza.todo.ui.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.alireza.todo.data.model.DatabaseTask
import com.alireza.todo.data.model.model.User
import com.alireza.todo.data.model.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val userDao = DatabaseTask.getDatabase(application).userDao()

    val userRepository: UserRepository
  // lateinit var userActive:LiveData<User>

    init {
        this.userRepository = UserRepository(userDao)
    }

    fun signupUser(user: User) {
        viewModelScope.launch() {
            userRepository.signupUser(user)
            Log.d("user","done")
        }
    }
    fun deleteUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.deleteUser(user)
        }
    }

    fun searchUser(username: String): LiveData<User?> {
         return userRepository.searchUser(username).asLiveData()
    }


}