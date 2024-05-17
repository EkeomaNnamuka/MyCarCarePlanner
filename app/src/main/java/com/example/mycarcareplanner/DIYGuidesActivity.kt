package com.example.mycarcareplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarcareplanner.databinding.ActivityDiyGuidesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DIYGuidesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDiyGuidesBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var guidesAdapter: DIYGuidesAdapter
    private val guidesList = mutableListOf<DIYGuide>()

    private lateinit var guidesListener: ValueEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiyGuidesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setupRecyclerView()

        fetchDIYGuides()

        binding.buttonAddGuide.setOnClickListener {
            val title = binding.editTextGuideTitle.text.toString()
            val content = binding.editTextGuideContent.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                saveGuideToFirebase(title, content)
            } else {
                Toast.makeText(this, "Please enter both title and content", Toast.LENGTH_SHORT).show()
            }
        }

        binding.buttonHome.setOnClickListener {
            navigateToMenu()
        }
    }

    private fun setupRecyclerView() {
        guidesAdapter = DIYGuidesAdapter(guidesList)
        binding.recyclerViewGuides.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewGuides.adapter = guidesAdapter
    }

    private fun saveGuideToFirebase(title: String, content: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val guideId = database.push().key
            if (guideId != null) {
                val guide = DIYGuide(title, content)
                database.child("users").child(currentUser.uid).child("guides").child(guideId).setValue(guide)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Guide saved successfully", Toast.LENGTH_SHORT).show()
                        guidesList.add(guide)
                        guidesAdapter.notifyDataSetChanged()
                        binding.editTextGuideTitle.text.clear()
                        binding.editTextGuideContent.text.clear()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Error saving guide: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun fetchDIYGuides() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val guidesRef = database.child("users").child(currentUser.uid).child("guides")

            // Remove previous listener if exists
            if (::guidesListener.isInitialized) {
                guidesRef.removeEventListener(guidesListener)
            }

            guidesListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    guidesList.clear()
                    for (dataSnapshot in snapshot.children) {
                        val guide = dataSnapshot.getValue(DIYGuide::class.java)
                        if (guide != null) {
                            guidesList.add(guide)
                        }
                    }
                    guidesAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@DIYGuidesActivity, "Failed to retrieve data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            }

            guidesRef.addValueEventListener(guidesListener)
        }
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}
