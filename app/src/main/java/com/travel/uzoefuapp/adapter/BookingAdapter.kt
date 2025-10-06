package com.travel.uzoefuapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.bookingActivities.BookSummaryActivity
import com.travel.uzoefuapp.bookingCompleteModel.BookingCompleteResponse
import com.travel.uzoefuapp.companyActivities.BookingProductActivity

class BookingAdapter(
    private val context: Context,
    private val status: String,
    private val bookingList: List<BookingCompleteResponse.Datum>
) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booking_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = bookingList[position]
        val imgBase = "https://mobappssolutions.in/uzoefu/public/images/activity_images/"
        holder.txtTitle.text = list.activityName
        holder.txtDate.text = list.date
        holder.txtPrice.text = "R ${list.total.toString()}"
        Glide.with(context)
            .load(imgBase + list.images)
            .placeholder(R.drawable.balloon)
            .error(R.drawable.balloon)
            .into(holder.imgActivity)

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
            intent.putExtra("bookingId", list.id.toString())
            context.startActivity(intent)
        }

        holder.txtRebook.setOnClickListener {
            val intent = Intent(context, BookingProductActivity::class.java)
            intent.putExtra("categoryId", list.activityId)
            // intent.putExtra("activeHours", list.todayHours.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val status: TextView = itemView.findViewById(R.id.txtStatus)
        val layout: LinearLayout = itemView.findViewById(R.id.linearLayout)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtDate: TextView = itemView.findViewById(R.id.txtDate)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val imgActivity: ImageView = itemView.findViewById(R.id.imgActivity)
        val txtRebook: TextView = itemView.findViewById(R.id.txtRebook)
    }
}
