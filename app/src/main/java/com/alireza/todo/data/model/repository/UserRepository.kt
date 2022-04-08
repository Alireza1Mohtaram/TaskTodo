package com.alireza.todo.data.model.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alireza.todo.data.model.dao.UserDao
import com.alireza.todo.data.model.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao : UserDao) {

    suspend fun signupUser(user: User){
        userDao.signupUser(user)
    }
    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }
       fun searchUser(username: String) : Flow<User?>{
      return userDao.loginInfo(usernameInput = username)
    }
}