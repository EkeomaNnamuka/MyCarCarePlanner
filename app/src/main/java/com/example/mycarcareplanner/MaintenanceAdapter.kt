package com.example.mycarcareplanner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MaintenanceAdapter(private val maintenanceList: List<Maintenance>) : RecyclerView.Adapter<MaintenanceAdapter.MaintenanceViewHolder>() {

    class MaintenanceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val taskTextView: TextView = itemView.findViewById(R.id.textViewTask)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_maintenance, parent, false)
        return MaintenanceViewHolder(view)
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        val maintenance = maintenanceList[position]
        holder.taskTextView.text = maintenance.task
        holder.dateTextView.text = maintenance.date
    }

    override fun getItemCount() = maintenanceList.size
}
