package com.example.mycarcareplanner

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarcareplanner.databinding.ItemCommunityGuideBinding

class CommunityGuidesAdapter(private val guidesList: List<DIYGuide>, private val context: Context) : RecyclerView.Adapter<CommunityGuidesAdapter.GuideViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuideViewHolder {
        val binding = ItemCommunityGuideBinding.inflate(LayoutInflater.from(context), parent, false)
        return GuideViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GuideViewHolder, position: Int) {
        val guide = guidesList[position]
        holder.bind(guide)
    }

    override fun getItemCount(): Int {
        return guidesList.size
    }

    inner class GuideViewHolder(private val binding: ItemCommunityGuideBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(guide: DIYGuide) {
            binding.textViewTitle.text = guide.title
            binding.textViewContent.text = guide.content

            binding.buttonShare.setOnClickListener {
                shareGuide(guide)
            }
        }

        private fun shareGuide(guide: DIYGuide) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "${guide.title}\n\n${guide.content}")
            }
            context.startActivity(Intent.createChooser(shareIntent, "Share guide via"))
        }
    }
}
