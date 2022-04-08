package com.alireza.todo.ui.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UserVmFactory (private val appContext: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val userVm = UserViewModel(application = appContext)
        return userVm as T
    }

    }
