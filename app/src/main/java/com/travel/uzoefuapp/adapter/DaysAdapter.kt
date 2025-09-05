package com.travel.uzoefuapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

data class DayItem(
    val dayName: String,
    var isSelected: Boolean = false
)

class DaysAdapter(
    private val context: Context,
    private val days: MutableList<DayItem>,
    private val onDaySelected: (DayItem, Int) -> Unit
) : RecyclerView.Adapter<DaysAdapter.DayViewHolder>() {

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnDay: TextView = itemView.findViewById(R.id.btnDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val day = days[position]
        holder.btnDay.text = day.dayName

        if (day.isSelected) {
            holder.itemView.setBackgroundResource(R.drawable.category_selected_background)
            holder.btnDay.setTextColor(ContextCompat.getColor(context, R.color.dark_cyan))
        } else {
            holder.itemView.setBackgroundResource(R.drawable.category_background)
            holder.btnDay.setTextColor(ContextCompat.getColor(context, R.color.gray))
        }

        holder.btnDay.isSelected = day.isSelected

        holder.btnDay.setOnClickListener {
            day.isSelected = !day.isSelected
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = days.size
}