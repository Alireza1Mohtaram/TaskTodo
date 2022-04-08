package com.alireza.todo.ui.adapters


import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.alireza.todo.R
import com.alireza.todo.data.model.model.Priority
import com.alireza.todo.data.model.model.Task
import com.alireza.todo.data.model.utils.ToDoDiffUtils

class TaskList : RecyclerView.Adapter<TaskList.MyViewHolder>() {
    var dataList = mutableListOf<Task>()

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title_txt: TextView = itemView.findViewById(R.id.title_txt)
        private val nameTxt: TextView = itemView.findViewById(R.id.nameTxt)
        private val description_txt: TextView = itemView.findViewById(R.id.description_txt)
        private val priority_indicator: CardView = itemView.findViewById(R.id.priority_indicator)
        private val nameTextLayout: CardView = itemView.findViewById(R.id.nameTxtLayout)
        private val imgTask: ImageView = itemView.findViewById(R.id.img_task)
        private val row_background: ConstraintLayout = itemView.findViewById(R.id.row_background)

        fun bind(position: Int) {
            title_txt.text = dataList[position].title
             // if (dataList[position].imgUri ==null){
            nameTxt.text = dataList[position].title[0].toString().uppercase()
           // }
//            else {
//
//                nameTxt.visibility = View.GONE
//                imgTask.visibility = View.VISIBLE
//                imgTask.setImageURI(dataList[position].imgUri?.toUri())
//            }
            description_txt.text = dataList[position].description
            row_background.setOnClickListener {
                it.findNavController().navigate(
                    R.id.action_hostFragment_to_updateFragment,
                    bundleOf("task" to dataList[position].id)
                )
            }
            val priority = dataList[position].priority
            when (priority) {
                Priority.HIGH -> priority_indicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        priority_indicator.context,
                        R.color.red
                    )
                )
                Priority.MEDIUM -> priority_indicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        priority_indicator.context,
                        R.color.yellow
                    )
                )
                Priority.LOW -> priority_indicator.setCardBackgroundColor(
                    ContextCompat.getColor(
                        priority_indicator.context,
                        R.color.green
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoData: List<Task>) {
        val toDoDiffUtils = ToDoDiffUtils(dataList, toDoData)
        val toDoDiffResults = DiffUtil.calculateDiff(toDoDiffUtils)
        this.dataList = toDoData as MutableList<Task>
        toDoDiffResults.dispatchUpdatesTo(this)
        notifyItemInserted(toDoData.size)
    }

}