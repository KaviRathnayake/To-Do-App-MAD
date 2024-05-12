package com.example.todo_app

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "task_item_table")
class TaskItem(
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "desc") var desc: String,
    @ColumnInfo(name = "dueTimeString") var dueTimeString: String?,
    @ColumnInfo(name = "completedDateString") var completedDateString: String?,
    @PrimaryKey(autoGenerate = true) var id: Int = 0)
{

    // Convert completedDateString to LocalDate
    @RequiresApi(Build.VERSION_CODES.O)
    fun completedDate(): LocalDate? = if(completedDateString == null) null else LocalDate.parse(completedDateString, dateFormatter)

    // Convert dueTimeString to LocalTime
    @RequiresApi(Build.VERSION_CODES.O)
    fun dueTime(): LocalTime? = if(dueTimeString == null) null else LocalTime.parse(dueTimeString, timeFormatter)

    // Check if the task is completed
    @RequiresApi(Build.VERSION_CODES.O)
    fun isCompleted() = completedDate() != null

    companion object{
        // DateTimeFormatter for time
        @RequiresApi(Build.VERSION_CODES.O)
        val timeFormatter: DateTimeFormatter = DateTimeFormatter.ISO_TIME

        // DateTimeFormatter for date
        @RequiresApi(Build.VERSION_CODES.O)
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_DATE
    }

}