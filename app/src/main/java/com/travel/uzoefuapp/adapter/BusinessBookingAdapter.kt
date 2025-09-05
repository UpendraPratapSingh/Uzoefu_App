package com.travel.uzoefuapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.bookingActivities.BookSummaryActivity
import com.travel.uzoefuapp.businessActivities.BusinessBookingSummaryActivity

class BusinessBookingAdapter(
    private val context: Context,
    private val status: String
) : RecyclerView.Adapter<BusinessBookingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.business_booking_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (status == "Cancelled") {
            holder.status.visibility = View.VISIBLE
            holder.status.text = "Cancelled"
            holder.status.setTextColor(ContextCompat.getColor(context, R.color.red))
        } else {
            holder.status.visibility = View.GONE
        }
        holder.itemView.setOnClickListener {
            val intent = Intent(context, BusinessBookingSummaryActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return 6
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val status: TextView = itemView.findViewById(R.id.txtStatus)
    }
}
