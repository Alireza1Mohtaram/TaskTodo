package com.alireza.todo.data.model.utils

import androidx.recyclerview.widget.DiffUtil
import com.alireza.todo.data.model.model.Task

class ToDoDiffUtils(
    private val oldList: List<Task>,
    private val newList: List<Task>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
                && oldList[oldItemPosition].title == newList[newItemPosition].title
                && oldList[oldItemPosition].description == newList[newItemPosition].description
                && oldList[oldItemPosition].priority == newList[newItemPosition].priority
                && oldList[oldItemPosition].dateTime == newList[newItemPosition].dateTime
                && oldList[oldItemPosition].priority == newList[newItemPosition].priority
    }
}