package com.example.mycarcareplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DIYGuidesAdapter(private val guidesList: List<DIYGuide>) : RecyclerView.Adapter<DIYGuidesAdapter.DIYGuideViewHolder>() {

    class DIYGuideViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val contentTextView: TextView = itemView.findViewById(R.id.textViewContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DIYGuideViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_guide, parent, false)
        return DIYGuideViewHolder(view)
    }

    override fun onBindViewHolder(holder: DIYGuideViewHolder, position: Int) {
        val guide = guidesList[position]
        holder.titleTextView.text = guide.title
        holder.contentTextView.text = guide.content
    }

    override fun getItemCount() = guidesList.size
}
