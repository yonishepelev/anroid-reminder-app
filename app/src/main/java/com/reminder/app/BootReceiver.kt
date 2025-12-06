package com.reminder.app

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.reminder.app.data.ReminderDatabase
import com.reminder.app.utils.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            CoroutineScope(Dispatchers.IO).launch {
                val database = ReminderDatabase.getDatabase(context)
                val reminders = database.reminderDao().getAllReminders().value ?: emptyList()
                val scheduler = AlarmScheduler(context)

                reminders.filter { it.isEnabled }.forEach { reminder ->
                    scheduler.scheduleAlarm(reminder)
                }
            }
        }
    }
}
