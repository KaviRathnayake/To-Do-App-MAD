package com.example.todo_app

import android.content.DialogInterface
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todo_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TaskItemClickListener {

    private lateinit var binding: ActivityMainBinding
    // Using viewModels delegate to initialize TaskViewModel
    private val taskViewModel: TaskViewModel by viewModels {
        TaskItemModelFactory((application as TodoApplication).repository)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //on click listener for new task button
        binding.newTask.setOnClickListener{
            NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")
        }

        // Set up RecyclerView
        setRecyclerView()

    }

    // Function to set up RecyclerView with TaskItemAdapter
    private fun setRecyclerView() {
        val mainActivity = this

        taskViewModel.taskItems.observe(this){
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = TaskItemAdapter(it, mainActivity)
            }
        }
    }

    // Override method to edit a task item
    override fun editTaskItem(taskItem: TaskItem) {
        NewTaskSheet(taskItem).show(supportFragmentManager,"newTaskTag")
    }

    // Override method to mark a task item as completed
    @RequiresApi(Build.VERSION_CODES.O)
    override fun completeTaskItem(taskItem: TaskItem) {
        taskViewModel.setCompleted(taskItem)
    }

    // Override method to mark a task item as incomplete
    @RequiresApi(Build.VERSION_CODES.O)
    override fun incompleteTaskItem(taskItem: TaskItem) {
        taskViewModel.setIncomplete(taskItem)
    }

    // Override method to delete a task item
    override fun deleteTaskItem(taskItem: TaskItem) {
        showDeleteConfirmationDialog(taskItem)
    }

    // Function to display delete confirmation dialog
    private fun showDeleteConfirmationDialog(taskItem: TaskItem) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Task")
            .setMessage("Are you sure you want to delete?")
            .setPositiveButton("Delete") { dialogInterface: DialogInterface, i: Int ->
                // User confirmed deletion, call deleteTaskItem
                taskViewModel.deleteTaskItem(taskItem)
                dialogInterface.dismiss()
            }
            .setNegativeButton("Cancel") { dialogInterface: DialogInterface, i: Int ->
                // User canceled deletion, dismiss dialog
                dialogInterface.dismiss()
            }
            .show()
    }
}