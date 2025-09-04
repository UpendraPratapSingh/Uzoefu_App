package com.travel.uzoefuapp.adapter

import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import java.util.Calendar

data class DayAvailability(
    val dayName: String,
    var startTime: String = "",
    var endTime: String = ""
)

class ActiveHoursAdapter(
    private val context: Context,
    private val days: MutableList<DayAvailability>
) : RecyclerView.Adapter<ActiveHoursAdapter.DayViewHolder>() {

    inner class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDayName: TextView = itemView.findViewById(R.id.tvDayName)
        val etStartTime: EditText = itemView.findViewById(R.id.etStartTime)
        val etEndTime: EditText = itemView.findViewById(R.id.etEndTime)
        val btnAdd: ImageView = itemView.findViewById(R.id.btnAdd)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_day_availability, parent, false)
        return DayViewHolder(view)
    }

    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {
        val item = days[position]

        holder.tvDayName.text = item.dayName
        holder.etStartTime.setText(item.startTime)
        holder.etEndTime.setText(item.endTime)

        holder.btnAdd.setOnClickListener {
            showStartEndTimePicker(holder.itemView.context) { start, end ->
                item.startTime = start
                item.endTime = end
                notifyItemChanged(position)
            }
        }
    }
    
    override fun getItemCount(): Int = days.size

    private fun showStartEndTimePicker(context: Context, onSelected: (String, String) -> Unit) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(context, { _, startHour, startMinute ->
            val start = String.format("%02d:%02d", startHour, startMinute)

            TimePickerDialog(context, { _, endHour, endMinute ->
                val end = String.format("%02d:%02d", endHour, endMinute)
                onSelected(start, end)
            }, hour, minute, true).show()

        }, hour, minute, true).show()
    }
}