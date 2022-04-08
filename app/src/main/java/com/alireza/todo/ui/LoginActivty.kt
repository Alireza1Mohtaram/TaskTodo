package com.alireza.todo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.alireza.todo.R
import com.alireza.todo.data.model.DatabaseTask
import com.alireza.todo.databinding.ActivityLoginBinding
import com.alireza.todo.ui.adapters.LoginViewPagerAdapter
import com.google.android.material.tabs.TabLayoutMediator

class LoginActivty : AppCompatActivity() {

     lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        setContentView(binding.root)
        val userDao =  DatabaseTask.getDatabase(applicationContext).userDao()
        Log.d("userDao","created")
        binding.viewPager2.adapter = LoginViewPagerAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(binding.tab,binding.viewPager2,{tab,pos ->
            when(pos){
                0->tab.text = "login"
                1->tab.text = "sign up"
            }
        }).attach()

    }
}