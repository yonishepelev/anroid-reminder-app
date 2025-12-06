package com.reminder.app

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.reminder.app.ui.ReminderAdapter
import com.reminder.app.ui.ReminderViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ReminderViewModel
    private lateinit var adapter: ReminderAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View
    private lateinit var fabAdd: FloatingActionButton

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            // Handle permission denial if needed
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        emptyView = findViewById(R.id.emptyView)
        fabAdd = findViewById(R.id.fabAdd)

        viewModel = ViewModelProvider(this)[ReminderViewModel::class.java]

        adapter = ReminderAdapter { reminder ->
            val intent = Intent(this, AddEditReminderActivity::class.java).apply {
                putExtra("REMINDER_ID", reminder.id)
            }
            startActivity(intent)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.allReminders.observe(this) { reminders ->
            adapter.submitList(reminders)
            if (reminders.isEmpty()) {
                emptyView.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyView.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

        fabAdd.setOnClickListener {
            startActivity(Intent(this, AddEditReminderActivity::class.java))
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
