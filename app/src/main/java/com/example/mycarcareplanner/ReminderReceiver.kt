package com.example.mycarcareplanner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val title = intent.getStringExtra("title")
        Toast.makeText(context, "Reminder: $title", Toast.LENGTH_LONG).show()
    }
}
