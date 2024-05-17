package com.example.mycarcareplanner

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycarcareplanner.databinding.ItemMaintenanceHistoryBinding

class MaintenanceHistoryAdapter(private val maintenanceList: List<MaintenanceHistory>) : RecyclerView.Adapter<MaintenanceHistoryAdapter.MaintenanceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MaintenanceViewHolder {
        val binding = ItemMaintenanceHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MaintenanceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MaintenanceViewHolder, position: Int) {
        holder.bind(maintenanceList[position])
    }

    override fun getItemCount(): Int {
        return maintenanceList.size
    }

    class MaintenanceViewHolder(private val binding: ItemMaintenanceHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(maintenance: MaintenanceHistory) {
            binding.textViewTask.text = "Task: ${maintenance.task}"
            binding.textViewDate.text = "Date: ${maintenance.date}"
            binding.textViewExpense.text = String.format("Expense: £%.2f", maintenance.expense)
        }
    }
}
