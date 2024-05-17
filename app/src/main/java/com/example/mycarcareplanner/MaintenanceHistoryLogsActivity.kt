package com.example.mycarcareplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarcareplanner.databinding.ActivityMaintenanceHistoryLogsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MaintenanceHistoryLogsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMaintenanceHistoryLogsBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var maintenanceAdapter: MaintenanceHistoryAdapter
    private val maintenanceList = mutableListOf<MaintenanceHistory>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMaintenanceHistoryLogsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setupRecyclerView()

        fetchMaintenanceHistory()

        binding.buttonAddMaintenance.setOnClickListener {
            val task = binding.editTextTask.text.toString()
            val date = binding.editTextDate.text.toString()
            val expense = binding.editTextExpense.text.toString().toDoubleOrNull()

            if (task.isNotEmpty() && date.isNotEmpty() && expense != null) {
                val maintenance = MaintenanceHistory(task, date, expense)
                saveMaintenance(maintenance)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonHome.setOnClickListener {
            navigateToMenu()
        }
    }

    private fun setupRecyclerView() {
        maintenanceAdapter = MaintenanceHistoryAdapter(maintenanceList)
        binding.recyclerViewMaintenance.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewMaintenance.adapter = maintenanceAdapter
    }

    private fun saveMaintenance(maintenance: MaintenanceHistory) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val maintenanceRef = database.child("users").child(currentUser.uid).child("maintenanceHistory").push()
            maintenanceRef.setValue(maintenance)
                .addOnSuccessListener {
                    Toast.makeText(this, "Maintenance added successfully", Toast.LENGTH_SHORT).show()
                    maintenanceList.add(maintenance)
                    maintenanceAdapter.notifyDataSetChanged()
                    clearInputFields()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error adding maintenance: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun fetchMaintenanceHistory() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val maintenanceRef = database.child("users").child(currentUser.uid).child("maintenanceHistory")

            maintenanceRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    maintenanceList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val maintenance = dataSnapshot.getValue(MaintenanceHistory::class.java)
                        if (maintenance != null) {
                            maintenanceList.add(maintenance)
                        }
                    }
                    maintenanceAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MaintenanceHistoryLogsActivity, "Failed to retrieve data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun clearInputFields() {
        binding.editTextTask.text.clear()
        binding.editTextDate.text.clear()
        binding.editTextExpense.text.clear()
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}
