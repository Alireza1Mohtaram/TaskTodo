package com.alireza.todo.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alireza.todo.R
import com.alireza.todo.data.model.model.Priority
import com.alireza.todo.data.model.model.State
import com.alireza.todo.data.model.model.Task
import com.alireza.todo.databinding.UpdateFragmentBinding
import com.alireza.todo.ui.viewmodels.TaskViewModel
import com.google.android.material.snackbar.Snackbar
import java.util.*

class UpdateFragment : Fragment(R.layout.update_fragment), DatePickerDialog.OnDateSetListener {

    val viewModel: TaskViewModel by viewModels()
    lateinit var binding: UpdateFragmentBinding
    var selectedDate: Date? = null
    var userCreator : String? = null
    var idTask : Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = UpdateFragmentBinding.bind(view)

        setDataInit()

        val listener: AdapterView.OnItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> {
                            (parent?.getChildAt(0) as TextView).setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.red
                                )
                            )
                        }
                        1 -> {
                            (parent?.getChildAt(0) as TextView).setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.yellow
                                )
                            )
                        }
                        2 -> {
                            (parent?.getChildAt(0) as TextView).setTextColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.green
                                )
                            )
                        }
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }

            }


        binding.prioritiesSpinner.onItemSelectedListener = listener

        binding.etDate.setOnClickListener {
            showDatePicker()
        }
        binding.updateTask.setOnClickListener {
            if (getTask() != null) {
                viewModel.updateTask(getTask()!!)
                Snackbar.make(requireView(), "Task update !", Snackbar.LENGTH_SHORT).show()
            } else Snackbar.make(requireView(), "Please insert all data valid", Snackbar.LENGTH_SHORT)
                .show()


        }
    }

    private fun setDataInit() {
        var taskid:Int? = requireArguments().getInt("task")
        if (taskid != null) {
            idTask = taskid
            viewModel.finTask(taskid).observe(viewLifecycleOwner){
                Log.d("data",it.toString())
                binding.titleEt.setText( it.title)
                binding.descriptionEt.setText( it.description)
                binding.etDate.setText(it.dateTime.toString())
                selectedDate = it.dateTime

                binding.prioritiesSpinner.setSelection( it.priority.ordinal)
                binding.stateSpinner.setSelection( it.state.ordinal)

                userCreator = it.userCreator

            }
        }
    }

    fun parsePriority(priority: String): Priority {
        return when (priority) {
            "High" -> {
                Priority.HIGH
            }
            "Medium" -> {
                Priority.MEDIUM
            }
            "Low" -> {
                Priority.LOW
            }
            else -> {
                Priority.LOW
            }
        }
    }
    fun parsePriorityToInt(priority: String): Priority {
        return when (priority) {
            "High" -> {
                Priority.HIGH
            }
            "Medium" -> {
                Priority.MEDIUM
            }
            "Low" -> {
                Priority.LOW
            }
            else -> {
                Priority.LOW
            }
        }
    }

    fun parseState(state: String): State {
        return when (state) {
            "Todo" -> {
                State.TODO
            }
            "Doing" -> {
                State.DOING
            }
            "Done" -> {
                State.DONE
            }
            else -> {
                State.DONE
            }
        }
    }

    private fun getTask(): Task? {

        if (!checkInputs()) return null

        val mPriority = binding.prioritiesSpinner.selectedItem.toString()
        val mState = binding.stateSpinner.selectedItem.toString()
        val mTitle = binding.titleEt.text.toString().trim()
        val mDescribe = binding.descriptionEt.text.toString().trim()
        val mDate = selectedDate


        return userCreator?.let {
            Task(
                idTask!!,
                mTitle,
                mDescribe,
                mDate!!,
                parsePriority(mPriority),
                parseState(mState),
                it
            )
        }
    }

    private fun checkInputs(): Boolean {
        return binding.titleEt.check()
                && binding.descriptionEt.check()
                && selectedDate != null
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance(TimeZone.getDefault())
        DatePickerDialog(
            requireContext(),
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onDateSet(datePicker: DatePicker?, year: Int, month: Int, day: Int) {
        selectedDate = Date(year, month, day)
        binding.etDate.setText("$year/$month/$day")
    }
    private fun EditText.check(): Boolean {
        return !this.text.isNullOrBlank()
    }


}



