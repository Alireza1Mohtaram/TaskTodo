package com.alireza.todo.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alireza.todo.R
import com.alireza.todo.data.model.model.State
import com.alireza.todo.data.model.model.Task
import com.alireza.todo.databinding.DoingFragmentBinding
import com.alireza.todo.ui.adapters.TaskList
import com.alireza.todo.ui.viewmodels.SharedVM
import com.alireza.todo.ui.viewmodels.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class DoingFragment : Fragment(R.layout.doing_fragment) {

    private val viewModel: TaskViewModel by viewModels()
    private val sharedViewModel: SharedVM by activityViewModels()
    private lateinit var binding: DoingFragmentBinding
    lateinit var sp: SharedPreferences
    lateinit var taskAdapter: TaskList
    var username: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.doing_fragment, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        taskAdapter = TaskList()

        viewModel.opreation.observe(viewLifecycleOwner) {
            Log.d("data", it.name)
            Toast.makeText(requireContext(), "$it is done", Toast.LENGTH_SHORT).show()
        }

        initRecyclerView()

        sp = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        if (sp.contains("username")) Log.d("data", "hasUsername")


    }

    private fun initRecyclerView() {
        binding.emptyViewDoing.visibility = View.VISIBLE
        with(binding.doingRecyclerview) {
            visibility = View.GONE
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = SlideInUpAnimator()
            scheduleLayoutAnimation()
        }
        swipeToDelete(binding.doingRecyclerview)

    }

    override fun onResume() {
        super.onResume()

        Log.d("data", "OnResume")
        getUser()

    }


    override fun onStop() {
        super.onStop()

        Log.d("data", "OnStop")

    }

    private fun getData() {
        sharedViewModel.allData.observe(viewLifecycleOwner) { data ->
            val filterd = data.filter {
                it.state == State.DOING
            }
            if (filterd.isEmpty().not()) {
                Log.d("data", filterd[0].toString())
                binding.emptyViewDoing.visibility = View.GONE
                binding.doingRecyclerview.visibility = View.VISIBLE
                taskAdapter.setData(filterd)
            }else{
                binding.emptyViewDoing.visibility = View.VISIBLE
                binding.doingRecyclerview.visibility = View.GONE
            }
        }
    }

    private fun getUser() {
        sp.getString("username", "user")?.let {
            Log.d("data", it)
            if (it == "admin") {
                Log.d("data", "admin")
                sharedViewModel.getAllTaskAdmin().observe(viewLifecycleOwner) { data ->
                    val filterd = data.filter {
                        it.state == State.DOING
                    }
                    if (filterd.isEmpty().not()) {
                        Log.d("data", filterd[0].toString())
                        binding.emptyViewDoing.visibility = View.GONE
                        binding.doingRecyclerview.visibility = View.VISIBLE
                        taskAdapter.setData(filterd)

                    }
                }
            } else {
                if (username != it) {
                    username = it
                    sharedViewModel.getAllTask(it)
                }
                getData()
            }
        }
    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback =
            object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val deletedItem = taskAdapter.dataList[viewHolder.adapterPosition]
                    // Delete Item
                    viewModel.deleteTask(deletedItem)
                    taskAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                    // Restore Deleted Item
                    restoreDeletedData(viewHolder, deletedItem, viewHolder.adapterPosition)
                    username?.let { sharedViewModel.getAllTask(it) }
                }
            }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(
        view: RecyclerView.ViewHolder,
        deletedData: Task,
        position: Int
    ) {
        val snackbar =
            Snackbar.make(view.itemView, "Deleted '${deletedData.title}'", Snackbar.LENGTH_LONG)
        snackbar.setAction("Undo") {
            viewModel.insertData(deletedData)
        }
        snackbar.show()
    }

}