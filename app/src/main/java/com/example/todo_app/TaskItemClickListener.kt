package com.example.todo_app

interface TaskItemClickListener {
    fun editTaskItem(taskItem: TaskItem)
    fun completeTaskItem(taskItem: TaskItem)
    fun incompleteTaskItem(taskItem: TaskItem)

    fun deleteTaskItem(taskItem: TaskItem)

}