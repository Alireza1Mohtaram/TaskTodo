package com.alireza.todo.ui.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.alireza.todo.R
import com.alireza.todo.data.model.model.Priority
import com.alireza.todo.data.model.model.State
import com.alireza.todo.data.model.model.Task
import com.alireza.todo.databinding.AddFragmentBinding
import com.alireza.todo.ui.viewmodels.TaskViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.util.*

class AddFragment : Fragment(R.layout.add_fragment), DatePickerDialog.OnDateSetListener {

    val viewModel: TaskViewModel by viewModels()
    lateinit var binding: AddFragmentBinding
    var selectedDate: Date? = null
    var imgUri:Uri? = null
    lateinit var sp: SharedPreferences
    val galleryLauncher = registerForActivityResult(ActivityResultContracts.OpenDocument()){
        Handler(Looper.getMainLooper()).post {
            binding.imgTask.setImageURI(it)
            imgUri = it
        }
    }
    val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()){
//        if (it){
//            imgUri  = uri
//        }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = AddFragmentBinding.bind(view)

        sp = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)

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
        binding.insetTask.setOnClickListener {
            if (getTask() != null) {
                viewModel.insertData(getTask()!!)
                Snackbar.make(requireView(), "Task inserted !", Snackbar.LENGTH_SHORT).show()
            } else Snackbar.make(requireView(), "Please insert all data", Snackbar.LENGTH_SHORT)
                .show()
        }
        binding.imgLayout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext()).
                    setTitle("Choose image").
                    setMessage("Choose your way").
                    setNeutralButton("Gallery"){dialog,which ->
                        galleryLauncher.launch(arrayOf("image/*" ))
                    }.setNegativeButton("cancel"){
                    dialog,which ->
            }.show()
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
        val mUser = sp.getString("username", "user")
        val imgUriString : String? = if (imgUri == null)  null
        else  imgUri.toString()


        return Task(
            0,
            mTitle,
            mDescribe,
            mDate!!,
            parsePriority(mPriority),
            parseState(mState),
            mUser!!,
            imgUriString
        )
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
        selectedDate  = Date(year, month, day)
        binding.etDate.setText("$year/$month/$day")
    }
    private fun EditText.check(): Boolean {
        return !this.text.isNullOrBlank()
    }


}



