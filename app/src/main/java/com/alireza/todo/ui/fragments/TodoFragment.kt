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
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alireza.todo.R
import com.alireza.todo.data.model.model.State
import com.alireza.todo.data.model.model.Task
import com.alireza.todo.databinding.TodoFragmentBinding
import com.alireza.todo.ui.adapters.TaskList
import com.alireza.todo.ui.viewmodels.SharedVM
import com.alireza.todo.ui.viewmodels.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class TodoFragment : Fragment() {

    private val viewModel: TaskViewModel by viewModels()
    private val sharedViewModel: SharedVM by activityViewModels()
    private lateinit var binding: TodoFragmentBinding
    lateinit var sp: SharedPreferences
    var taskAdapter: TaskList = TaskList()
    var username: String? = null
    private var layoutManager = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.todo_fragment, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.opreation.observe(viewLifecycleOwner) {
            Log.d("data", it.name)
            Toast.makeText(requireContext(), "$it is done", Toast.LENGTH_SHORT).show()
        }

        initRecyclerView()

        sp = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        if (sp.contains("username")) Log.d("data", "hasUsername")

    }

    private fun initRecyclerView() {
        binding.emptyView.visibility = View.VISIBLE
        with(binding.todoRecyclerview) {
            visibility = View.GONE
            adapter = taskAdapter
            layoutManager = LinearLayoutManager(requireContext())
            itemAnimator = SlideInUpAnimator()
            scheduleLayoutAnimation()
        }
        swipeToDelete(binding.todoRecyclerview)

    }

    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallback = object : ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
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
                restoreDeletedData(viewHolder, deletedItem , viewHolder.adapterPosition)
                username?.let { sharedViewModel.getAllTask(it) }
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: RecyclerView.ViewHolder, deletedItem: Task , position :Int ) {
        val snackBar = Snackbar.make(
            view.itemView, "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction("Undo") {
            viewModel.insertData(deletedItem)
        }.setActionTextColor(resources.getColor(R.color.primary_row_data))
        snackBar.show()
    }

    override fun onResume() {
        super.onResume()

        Log.d("data", "OnResume")
        getUser()

    }

    private fun getData() {
        sharedViewModel.allData.observe(viewLifecycleOwner) { data ->
            val filterd = data.filter {
                it.state == State.TODO
            }
            if (filterd.isEmpty().not()) {
                Log.d("data", filterd[0].toString())
                binding.emptyView.visibility = View.GONE
                binding.todoRecyclerview.visibility = View.VISIBLE
                taskAdapter.setData(filterd)
            }else{
                binding.emptyView.visibility = View.VISIBLE
                binding.todoRecyclerview.visibility = View.GONE
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("data", "OnStop")
    }

    private fun getUser() {
        sp.getString("username", "user")?.let {
            Log.d("data", it)
            if (it == "admin") {
                Log.d("data", "admin")
                sharedViewModel.getAllTaskAdmin().observe(viewLifecycleOwner) { data ->
                    val filterd = data.filter {
                        it.state == State.TODO
                    }
                    if (filterd.isEmpty().not()) {
                        Log.d("data", filterd[0].toString())
                        binding.emptyView.visibility = View.GONE
                        binding.todoRecyclerview.visibility = View.VISIBLE
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuF = inflater.inflate(R.menu.host_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.delete_all_item -> {
                sharedViewModel.deleteAllTask()
            }
            R.id.high_priority_item -> {
                username?.let {
                    viewModel.sortByHighPriority(it).observe(viewLifecycleOwner) {
                        taskAdapter = TaskList()
                        val filterd = it.filter {
                            it.state == State.TODO
                        }
                        taskAdapter.setData(filterd)
                    }
                }
            }
            R.id.low_priority_item -> {
                username?.let {
                    viewModel.sortByLowPriority(it).observe(viewLifecycleOwner) {
                        val filterd = it.filter {
                            it.state == State.TODO
                        }
                        taskAdapter.setData(filterd)
                    }
                }
            }
            R.id.layout_manager_item ->{
                if (layoutManager){
                    layoutManager = false
                    item.icon = resources.getDrawable(R.drawable.ic_grid_row)
                    binding.todoRecyclerview.layoutManager = LinearLayoutManager(requireContext())
                    binding.todoRecyclerview.adapter = taskAdapter
                }
                else {
                    layoutManager = true
                    item.icon = resources.getDrawable(R.drawable.ic_linear_row)
                    binding.todoRecyclerview.layoutManager =
                            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                    binding.todoRecyclerview.adapter = taskAdapter

                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


}