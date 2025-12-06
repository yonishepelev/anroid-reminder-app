package com.reminder.app

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import com.reminder.app.data.Reminder
import com.reminder.app.data.RepeatType
import com.reminder.app.ui.ReminderViewModel
import com.reminder.app.utils.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddEditReminderActivity : AppCompatActivity() {

    private lateinit var viewModel: ReminderViewModel
    private lateinit var etTitle: TextInputEditText
    private lateinit var etDescription: TextInputEditText
    private lateinit var btnSelectDate: Button
    private lateinit var btnSelectTime: Button
    private lateinit var spinnerRepeat: Spinner
    private lateinit var btnSave: Button
    private lateinit var btnDelete: Button

    private var calendar = Calendar.getInstance()
    private var editingReminder: Reminder? = null
    private var reminderId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_reminder)

        viewModel = ViewModelProvider(this)[ReminderViewModel::class.java]

        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        btnSelectDate = findViewById(R.id.btnSelectDate)
        btnSelectTime = findViewById(R.id.btnSelectTime)
        spinnerRepeat = findViewById(R.id.spinnerRepeat)
        btnSave = findViewById(R.id.btnSave)
        btnDelete = findViewById(R.id.btnDelete)

        setupSpinner()
        setupButtons()

        reminderId = intent.getIntExtra("REMINDER_ID", -1)
        if (reminderId != -1) {
            title = getString(R.string.edit_reminder)
            btnDelete.visibility = View.VISIBLE
            loadReminder()
        } else {
            title = getString(R.string.add_reminder)
        }

        updateDateButton()
        updateTimeButton()
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.repeat_options,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerRepeat.adapter = adapter
    }

    private fun setupButtons() {
        btnSelectDate.setOnClickListener {
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, month)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateButton()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnSelectTime.setOnClickListener {
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)
                    updateTimeButton()
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            ).show()
        }

        btnSave.setOnClickListener {
            saveReminder()
        }

        btnDelete.setOnClickListener {
            deleteReminder()
        }
    }

    private fun loadReminder() {
        CoroutineScope(Dispatchers.IO).launch {
            val reminder = viewModel.allReminders.value?.find { it.id == reminderId }
            withContext(Dispatchers.Main) {
                reminder?.let {
                    editingReminder = it
                    etTitle.setText(it.title)
                    etDescription.setText(it.description)
                    calendar.timeInMillis = it.timeInMillis
                    spinnerRepeat.setSelection(it.repeatType.value)
                    updateDateButton()
                    updateTimeButton()
                }
            }
        }
    }

    private fun updateDateButton() {
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        btnSelectDate.text = String.format("%02d.%02d.%04d", day, month, year)
    }

    private fun updateTimeButton() {
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        btnSelectTime.text = String.format("%02d:%02d", hour, minute)
    }

    private fun saveReminder() {
        val title = etTitle.text.toString().trim()
        if (title.isEmpty()) {
            Toast.makeText(this, "Введите название напоминания", Toast.LENGTH_SHORT).show()
            return
        }

        val description = etDescription.text.toString().trim()
        val repeatType = RepeatType.fromInt(spinnerRepeat.selectedItemPosition)

        val reminder = if (editingReminder != null) {
            editingReminder!!.copy(
                title = title,
                description = description,
                timeInMillis = calendar.timeInMillis,
                repeatType = repeatType
            )
        } else {
            Reminder(
                title = title,
                description = description,
                timeInMillis = calendar.timeInMillis,
                repeatType = repeatType
            )
        }

        if (editingReminder != null) {
            viewModel.update(reminder)
            val scheduler = AlarmScheduler(this)
            scheduler.cancelAlarm(reminder.id)
            scheduler.scheduleAlarm(reminder)
            Toast.makeText(this, "Напоминание обновлено", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            viewModel.insert(reminder) { id ->
                val newReminder = reminder.copy(id = id.toInt())
                val scheduler = AlarmScheduler(this)
                scheduler.scheduleAlarm(newReminder)
                Toast.makeText(this, "Напоминание создано", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun deleteReminder() {
        editingReminder?.let {
            viewModel.delete(it)
            val scheduler = AlarmScheduler(this)
            scheduler.cancelAlarm(it.id)
            Toast.makeText(this, "Напоминание удалено", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
