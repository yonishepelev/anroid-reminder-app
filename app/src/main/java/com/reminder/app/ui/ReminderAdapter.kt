package com.reminder.app.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.reminder.app.R
import com.reminder.app.data.Reminder
import com.reminder.app.data.RepeatType
import java.text.SimpleDateFormat
import java.util.*

class ReminderAdapter(
    private val onItemClick: (Reminder) -> Unit
) : ListAdapter<Reminder, ReminderAdapter.ReminderViewHolder>(ReminderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReminderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reminder, parent, false)
        return ReminderViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReminderViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class ReminderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val tvRepeat: TextView = itemView.findViewById(R.id.tvRepeat)

        fun bind(reminder: Reminder, onItemClick: (Reminder) -> Unit) {
            tvTitle.text = reminder.title
            tvDescription.text = reminder.description
            tvDescription.visibility = if (reminder.description.isEmpty()) View.GONE else View.VISIBLE

            val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            tvTime.text = dateFormat.format(Date(reminder.timeInMillis))

            tvRepeat.text = when (reminder.repeatType) {
                RepeatType.NONE -> ""
                RepeatType.DAILY -> "Каждый день"
                RepeatType.WEEKLY -> "Каждую неделю"
                RepeatType.MONTHLY -> "Каждый месяц"
                RepeatType.YEARLY -> "Каждый год"
                RepeatType.WEEKDAYS -> "По будням"
                RepeatType.WEEKENDS -> "По выходным"
            }
            tvRepeat.visibility = if (reminder.repeatType == RepeatType.NONE) View.GONE else View.VISIBLE

            itemView.setOnClickListener { onItemClick(reminder) }
        }
    }

    class ReminderDiffCallback : DiffUtil.ItemCallback<Reminder>() {
        override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
            return oldItem == newItem
        }
    }
}
