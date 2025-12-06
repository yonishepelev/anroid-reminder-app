package com.reminder.app.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.reminder.app.ReminderReceiver
import com.reminder.app.data.Reminder
import com.reminder.app.data.RepeatType
import java.util.*

class AlarmScheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun scheduleAlarm(reminder: Reminder) {
        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra("REMINDER_ID", reminder.id)
            putExtra("REMINDER_TITLE", reminder.title)
            putExtra("REMINDER_DESC", reminder.description)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = getNextTriggerTime(reminder)

        when (reminder.repeatType) {
            RepeatType.NONE -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
            RepeatType.DAILY -> {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )
            }
            RepeatType.WEEKLY -> {
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    AlarmManager.INTERVAL_DAY * 7,
                    pendingIntent
                )
            }
            RepeatType.MONTHLY -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
            RepeatType.YEARLY -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
            RepeatType.WEEKDAYS, RepeatType.WEEKENDS -> {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        }
    }

    fun cancelAlarm(reminderId: Int) {
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }

    private fun getNextTriggerTime(reminder: Reminder): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = reminder.timeInMillis
        }

        val now = System.currentTimeMillis()

        when (reminder.repeatType) {
            RepeatType.WEEKDAYS -> {
                while (calendar.timeInMillis <= now ||
                       calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY ||
                       calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            RepeatType.WEEKENDS -> {
                while (calendar.timeInMillis <= now ||
                       (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY &&
                        calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
                    calendar.add(Calendar.DAY_OF_MONTH, 1)
                }
            }
            RepeatType.MONTHLY -> {
                while (calendar.timeInMillis <= now) {
                    calendar.add(Calendar.MONTH, 1)
                }
            }
            RepeatType.YEARLY -> {
                while (calendar.timeInMillis <= now) {
                    calendar.add(Calendar.YEAR, 1)
                }
            }
            else -> {
                if (calendar.timeInMillis <= now && reminder.repeatType == RepeatType.DAILY) {
                    while (calendar.timeInMillis <= now) {
                        calendar.add(Calendar.DAY_OF_MONTH, 1)
                    }
                } else if (calendar.timeInMillis <= now && reminder.repeatType == RepeatType.WEEKLY) {
                    while (calendar.timeInMillis <= now) {
                        calendar.add(Calendar.DAY_OF_MONTH, 7)
                    }
                }
            }
        }

        return calendar.timeInMillis
    }
}
