package com.example.mycarcareplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarcareplanner.databinding.ActivityMaintenanceSchedulesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MaintenanceSchedulesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaintenanceSchedulesBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var maintenanceAdapter: MaintenanceAdapter
    private val maintenanceList = mutableListOf<Maintenance>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaintenanceSchedulesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setupRecyclerView()

        fetchMaintenanceSchedules()

        binding.buttonAddMaintenance.setOnClickListener {
            val task = binding.editTextTask.text.toString()
            val date = binding.editTextDate.text.toString()

            if (task.isNotEmpty() && date.isNotEmpty()) {
                val maintenance = Maintenance(task, date)
                saveMaintenanceToDatabase(maintenance)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonHome.setOnClickListener {
            navigateToMenu()
        }
    }

    private fun setupRecyclerView() {
        maintenanceAdapter = MaintenanceAdapter(maintenanceList)
        binding.recyclerViewMaintenance.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMaintenance.adapter = maintenanceAdapter
    }

    private fun fetchMaintenanceSchedules() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            database.child("users").child(currentUser.uid).child("maintenanceSchedules")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        maintenanceList.clear()
                        for (dataSnapshot in snapshot.children) {
                            val maintenance = dataSnapshot.getValue(Maintenance::class.java)
                            if (maintenance != null) {
                                maintenanceList.add(maintenance)
                            }
                        }
                        maintenanceAdapter.notifyDataSetChanged()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MaintenanceSchedulesActivity, "Failed to retrieve data: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun saveMaintenanceToDatabase(maintenance: Maintenance) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val maintenanceId = database.push().key
            if (maintenanceId != null) {
                database.child("users").child(currentUser.uid).child("maintenanceSchedules").child(maintenanceId).setValue(maintenance)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Maintenance schedule added successfully", Toast.LENGTH_SHORT).show()
                        binding.editTextTask.text.clear()
                        binding.editTextDate.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error adding maintenance schedule: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}
