package com.example.mycarcareplanner

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarcareplanner.databinding.ActivityNotificationsBinding
import java.util.*

class NotificationsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsBinding
    private lateinit var reminderAdapter: ReminderAdapter
    private val reminderList = mutableListOf<Reminder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()

        binding.buttonAddReminder.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val time = binding.editTextTime.text.toString()

            if (title.isNotEmpty() && time.isNotEmpty()) {
                val reminder = Reminder(title, time)
                saveReminder(reminder)
                addEventToCalendar(reminder)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonHome.setOnClickListener {
            navigateToMenu()
        }
    }

    private fun setupRecyclerView() {
        reminderAdapter = ReminderAdapter(reminderList)
        binding.recyclerViewReminders.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewReminders.adapter = reminderAdapter
    }

    private fun saveReminder(reminder: Reminder) {
        reminderList.add(reminder)
        reminderAdapter.notifyDataSetChanged()

        binding.editTextTitle.text.clear()
        binding.editTextTime.text.clear()
    }

    private fun addEventToCalendar(reminder: Reminder) {
        val calendar = Calendar.getInstance()
        val timeParts = reminder.time.split(":")
        calendar.set(Calendar.HOUR_OF_DAY, timeParts[0].toInt())
        calendar.set(Calendar.MINUTE, timeParts[1].toInt())
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, reminder.title)
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calendar.timeInMillis)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calendar.timeInMillis + 60 * 60 * 1000) // 1 hour duration
            putExtra(CalendarContract.Events.HAS_ALARM, 1)
        }

        startActivity(intent)
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}
