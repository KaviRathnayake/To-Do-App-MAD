package com.example.todo_app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.time.LocalDate

class TaskViewModel(private val repository: TaskItemRepository): ViewModel() {

    // LiveData to hold list of task items
    var taskItems: LiveData<List<TaskItem>> =repository.allTaskItems.asLiveData()

    // Function to add a new task item
    fun addTaskItem(newTask: TaskItem) = viewModelScope.launch {
        repository.insertTaskItem(newTask)
    }

    // Function to update an existing task item
    fun updateTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        repository.updateTaskItem(taskItem)
    }

    // Function to delete a task item
    fun deleteTaskItem(taskItem: TaskItem) = viewModelScope.launch {
        repository.deleteTaskItem(taskItem)
    }

    // Function to mark a task item as completed
    @RequiresApi(Build.VERSION_CODES.O)
    fun setCompleted(taskItem: TaskItem) = viewModelScope.launch {
        if(!taskItem.isCompleted()){
            taskItem.completedDateString = TaskItem.dateFormatter.format(LocalDate.now())
        }
        repository.updateTaskItem(taskItem)
    }


    // Function to mark a task item as incomplete
    @RequiresApi(Build.VERSION_CODES.O)
    fun setIncomplete(taskItem: TaskItem) = viewModelScope.launch {
        if (taskItem.isCompleted()) {
            // Reset completedDateString to null
            taskItem.completedDateString = null
            // Update task item in the database
            repository.updateTaskItem(taskItem)
        }
    }

}

// Factory class for creating TaskViewModel instances
class TaskItemModelFactory(private  val repository: TaskItemRepository): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TaskViewModel::class.java))
            return TaskViewModel(repository) as T

        throw IllegalArgumentException("Unknown Class for View Model")
    }
}