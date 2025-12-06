package com.reminder.app.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.reminder.app.data.Reminder
import com.reminder.app.data.ReminderDatabase
import com.reminder.app.data.ReminderRepository
import kotlinx.coroutines.launch

class ReminderViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ReminderRepository
    val allReminders: LiveData<List<Reminder>>

    init {
        val reminderDao = ReminderDatabase.getDatabase(application).reminderDao()
        repository = ReminderRepository(reminderDao)
        allReminders = repository.allReminders
    }

    fun insert(reminder: Reminder, onSuccess: (Long) -> Unit) = viewModelScope.launch {
        val id = repository.insert(reminder)
        onSuccess(id)
    }

    fun update(reminder: Reminder) = viewModelScope.launch {
        repository.update(reminder)
    }

    fun delete(reminder: Reminder) = viewModelScope.launch {
        repository.delete(reminder)
    }
}
