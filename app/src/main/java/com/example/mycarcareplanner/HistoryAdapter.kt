package com.example.mycarcareplanner

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mycarcareplanner.databinding.ActivityMenuBinding

class HistoryAdapter : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonMaintenanceSchedules.setOnClickListener {
            startActivity(Intent(this, MaintenanceSchedulesActivity::class.java))
        }

        binding.buttonNotifications.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }

        binding.buttonMaintenanceHistoryLogs.setOnClickListener {
            startActivity(Intent(this, MaintenanceHistoryLogsActivity::class.java))
        }

        binding.buttonDIYGuides.setOnClickListener {
            startActivity(Intent(this, DIYGuidesActivity::class.java))
        }

        binding.buttonCommunityInteraction.setOnClickListener {
            startActivity(Intent(this, CommunityInteractionActivity::class.java))
        }
    }
}
