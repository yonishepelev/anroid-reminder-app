package com.reminder.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String = "",
    val timeInMillis: Long,
    val repeatType: RepeatType = RepeatType.NONE,
    val isEnabled: Boolean = true
)
