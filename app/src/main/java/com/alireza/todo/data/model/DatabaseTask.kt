package com.alireza.todo.data.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alireza.todo.data.model.dao.TaskDao
import com.alireza.todo.data.model.dao.UserDao
import com.alireza.todo.data.model.model.Task
import com.alireza.todo.data.model.model.User
import com.alireza.todo.data.model.utils.Converter

@Database(entities = [ User::class , Task::class], version = 1, exportSchema = true)
@TypeConverters(Converter::class)
abstract class DatabaseTask : RoomDatabase(){
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
    companion object{
        private var INSTANCE:DatabaseTask?=null
       fun getDatabase(context: Context) : DatabaseTask{
           val temp = INSTANCE
           if (temp != null ){
               return temp
           }
           synchronized(this){
               val instance = Room.databaseBuilder(context.applicationContext,
               DatabaseTask::class.java,
                   "task_manager"
                   ).build()
               INSTANCE = instance
               return instance
           }
       }
    }

}