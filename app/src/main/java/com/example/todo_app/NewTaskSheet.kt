package com.example.todo_app

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.todo_app.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalTime

class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueTime: LocalTime? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()

        // Setting up UI elements based on whether taskItem is provided (edit mode) or not (new task mode)
        if(taskItem != null){
            binding.taskTitle.text = "Edit Task"
            val editable = Editable.Factory.getInstance()
            binding.name.text = editable.newEditable(taskItem!!.name)
            binding.desc.text = editable.newEditable(taskItem!!.desc)

            if(taskItem!!.dueTime() != null){
                dueTime = taskItem!!.dueTime()!!
                updateTimeButtonText()
            }

        }else{
            binding.taskTitle.text = "New Task"
        }

        // Initializing ViewModel
        taskViewModel = ViewModelProvider(activity).get(TaskViewModel::class.java)

        // Click listener for save button
        binding.saveButton.setOnClickListener{
            saveAction()
        }

        // Click listener for time picker button
        binding.timePickerButton.setOnClickListener{
            openTimePicker()
        }
    }

    // Opens a time picker dialog to select due time
    @RequiresApi(Build.VERSION_CODES.O)
    private fun openTimePicker() {
        if(dueTime == null){
            dueTime = LocalTime.now()
        }

        val listener = TimePickerDialog.OnTimeSetListener{ _, selectedHour, selectedMinute ->
            dueTime = LocalTime.of(selectedHour,selectedMinute)
            updateTimeButtonText()
        }
        val dialog = TimePickerDialog(activity, listener, dueTime!!.hour, dueTime!!.minute, true)
        dialog.setTitle("Task Due")
        dialog.show()
    }

    // Updates the text of the time picker button with the selected due time
    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTimeButtonText() {
        binding.timePickerButton.text = String.format("%02d:%02d", dueTime!!.hour, dueTime!!.minute)
    }

    // Handles save action for creating or updating a task
    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveAction() {

        val name = binding.name.text.toString()
        val desc = binding.desc.text.toString()

        val dueTimeString = if(dueTime == null) null else TaskItem.timeFormatter.format(dueTime)

        if(taskItem == null){
            val newtask = TaskItem(name, desc, dueTimeString,null)
            taskViewModel.addTaskItem(newtask)
        }else{
            // Updating an existing task
            taskItem!!.name = name
            taskItem!!.desc = desc
            taskItem!!.dueTimeString = dueTimeString
            taskViewModel.updateTaskItem(taskItem!!)
        }

        // Clearing input fields and dismissing the bottom sheet
        binding.name.setText("")
        binding.desc.setText("")
        dismiss()
    }

    // Inflating the layout for the bottom sheet fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewTaskSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

}