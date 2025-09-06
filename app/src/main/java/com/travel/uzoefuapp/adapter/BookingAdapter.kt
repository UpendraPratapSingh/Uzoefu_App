package com.travel.uzoefuapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.bookingActivities.BookSummaryActivity

class BookingAdapter(
    private val context: Context, private val status: String
) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booking_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (status) {
            "Active" -> {
                // show active design
                holder.status.text = "Active"
            }

            "Past" -> {
                // show past design
                holder.status.text = "Completed"
            }

            "Cancelled" -> {
                // show cancelled design
                holder.status.text = "Cancelled"
                holder.status.setTextColor(ContextCompat.getColor(context, R.color.red))
                holder.layout.setBackgroundColor(ContextCompat.getColor(context, R.color.light_red))
            }
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, BookSummaryActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return 6
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val status = itemView.findViewById<TextView>(R.id.txtStatus)
        val layout = itemView.findViewById<LinearLayout>(R.id.linearLayout)
    }
}
