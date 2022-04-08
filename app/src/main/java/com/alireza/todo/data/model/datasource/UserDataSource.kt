package com.alireza.todo.data.model.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alireza.todo.data.model.dao.UserDao
import com.alireza.todo.data.model.model.User
import kotlinx.coroutines.flow.Flow

class UserDataSource (private val userDao: UserDao) {

    var user = MutableLiveData<User>()

    suspend fun signupUser(user: User){
        userDao.signupUser(user)
    }
    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }
     fun searchUser(username: String) : Flow<User?> {
        return userDao.loginInfo(usernameInput = username)
    }
}