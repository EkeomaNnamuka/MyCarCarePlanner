package com.example.mycarcareplanner

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mycarcareplanner.databinding.ActivityCommunityInteractionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class CommunityInteractionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCommunityInteractionBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var guidesAdapter: CommunityGuidesAdapter
    private val guidesList = mutableListOf<DIYGuide>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCommunityInteractionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setupRecyclerView()

        fetchCommunityGuides()

        binding.buttonHome.setOnClickListener {
            navigateToMenu()
        }
    }

    private fun setupRecyclerView() {
        guidesAdapter = CommunityGuidesAdapter(guidesList, this)
        binding.recyclerViewCommunity.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewCommunity.adapter = guidesAdapter
    }

    private fun fetchCommunityGuides() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val guidesRef = database.child("users").child(currentUser.uid).child("guides")

            guidesRef.addValueEventListener(object : ValueEventListener {
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
                    Toast.makeText(this@CommunityInteractionActivity, "Failed to retrieve data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish()
    }
}
