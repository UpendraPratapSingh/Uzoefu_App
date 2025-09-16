package com.travel.uzoefuapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.SelectDestinationActivity
import com.travel.uzoefuapp.activityModl.ActivityResponse
import com.travel.uzoefuapp.bookingActivities.BookingDetailActivity
import com.travel.uzoefuapp.companyActivities.BookingProductActivity
import com.travel.uzoefuapp.companyActivities.CompanyLandingActivity

class SelectedDestinationAdapter(
    val context: Context,
    private val activityList: List<ActivityResponse.Datum>
) :
    RecyclerView.Adapter<SelectedDestinationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.selected_destination_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = activityList[position]

        val imagePath = "https://mobappssolutions.in/uzoefu/public/images/activity_images/"

        holder.titleText.text = list.name
        holder.timing.text = list.todayHours
        holder.rating.text = list.rating
        holder.price.text = "R ${list.activityPrice.toString()}"

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(imagePath + list.image)
            .placeholder(R.drawable.balloon)
            .error(R.drawable.balloon)
            .into(holder.image)


        holder.itemView.setOnClickListener {
            val intent = Intent(context, BookingProductActivity::class.java)
                intent.putExtra("categoryId", list.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.forYouCountryImg)
        val titleText = itemView.findViewById<TextView>(R.id.forYouDestName)
        val timing = itemView.findViewById<TextView>(R.id.noOfDays)
        val rating = itemView.findViewById<TextView>(R.id.priceTxt)
        val price = itemView.findViewById<TextView>(R.id.priceTxt1)
    }
}