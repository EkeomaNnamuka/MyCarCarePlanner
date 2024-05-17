package com.example.mycarcareplanner

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mycarcareplanner.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.buttonSaveCar.setOnClickListener {
            val make = binding.editTextMake.text.toString()
            val model = binding.editTextModel.text.toString()
            val year = binding.editTextYear.text.toString().toIntOrNull()
            val mileage = binding.editTextMileage.text.toString().toIntOrNull()

            if (make.isNotEmpty() && model.isNotEmpty() && year != null && mileage != null) {
                val car = Car(make, model, year, mileage)
                saveCarToDatabase(car)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        checkUserAndNavigate()
    }

    private fun checkUserAndNavigate() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            database.child("cars").child(currentUser.uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            navigateToMenu()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@MainActivity, "Failed to retrieve data: ${error.message}", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    private fun saveCarToDatabase(car: Car) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            database.child("cars").child(currentUser.uid).setValue(car)
                .addOnSuccessListener {
                    Toast.makeText(this, "Car saved successfully", Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", "Car details saved: $car")
                    navigateToMenu()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error saving car: ${e.message}", Toast.LENGTH_SHORT).show()
                    Log.d("MainActivity", "Error saving car: ${e.message}")
                }
        }
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }

    data class Car(val make: String = "", val model: String = "", val year: Int = 0, val mileage: Int = 0)
}
