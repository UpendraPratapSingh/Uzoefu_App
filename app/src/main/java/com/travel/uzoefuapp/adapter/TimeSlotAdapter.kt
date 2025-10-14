package com.travel.uzoefuapp.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

class TimeSlotAdapter(
    private var timeSlots: List<String>,
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder>() {

    private var selectedPosition = -1

    inner class TimeSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnTime: Button = itemView.findViewById(R.id.btnTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_time_slot, parent, false)
        return TimeSlotViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: TimeSlotViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val time = timeSlots[position]
        holder.btnTime.text = time

        holder.btnTime.setBackgroundResource(
            if (position == selectedPosition) R.drawable.bg_time_slot_selected
            else R.drawable.bg_time_slot_unselected
        )

        holder.btnTime.setOnClickListener {
            val previousPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(previousPosition)
            notifyItemChanged(selectedPosition)

            onClick(time)
        }
    }

    override fun getItemCount(): Int = timeSlots.size

    fun updateList(newList: List<String>) {
        timeSlots = newList
        selectedPosition = -1
        notifyDataSetChanged()
    }
}

