package com.example.todo_app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TaskItem::class], version = 1, exportSchema = false)
abstract class TaskItemDatabase: RoomDatabase() {
    abstract fun taskItemDao(): TaskItemDao

    companion object{

        // Volatile variable to ensure visibility of changes across threads
        @Volatile
        private var INSTANCE: TaskItemDatabase? = null

        // Method to get a database instance
        fun getDatabase(context: Context): TaskItemDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskItemDatabase::class.java,
                    "task_item_database" // Name of the database file
                ).build()

                INSTANCE = instance
                instance
            }

        }
    }
}