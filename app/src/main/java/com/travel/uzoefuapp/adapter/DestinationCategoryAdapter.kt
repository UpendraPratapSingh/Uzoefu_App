package com.travel.uzoefuapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.SelectDestinationActivity
import com.travel.uzoefuapp.discoverDestinationModel.DiscoverDestinationResponse


class DestinationCategoryAdapter(
    private val context: Context,
    private val categories: List<DiscoverDestinationResponse.Datum>
) : RecyclerView.Adapter<DestinationCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.categories_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        //icon.setImageResource(category.imageRes)
        holder.name.text = category.branchName
        holder.count.text = "(${category.activityCount})"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SelectDestinationActivity::class.java)
            intent.putExtra("categoryName", "${category.branchName} (${category.activityCount})")
            intent.putExtra("branchId", category.branchId.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = categories.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon = itemView.findViewById<ImageView>(R.id.placeIcon)
        val name = itemView.findViewById<TextView>(R.id.placeTitle)
        val count = itemView.findViewById<TextView>(R.id.placeCount)

    }
}