package com.travel.uzoefuapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

class FavouriteAdapter(val context: Context) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    private val selectedPositions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favourite_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val categoryName = "Near Me"

        holder.categoryText.text = categoryName

        // Change background & text color if selected
        if (selectedPositions.contains(position)) {
            holder.itemView.setBackgroundResource(R.drawable.category_selected_background)
            holder.categoryText.setTextColor(ContextCompat.getColor(context, R.color.dark_cyan))
            holder.categoryIcon.setColorFilter(ContextCompat.getColor(context, R.color.dark_cyan))
        } else {
            holder.itemView.setBackgroundResource(R.drawable.category_background) // default
            holder.categoryText.setTextColor(ContextCompat.getColor(context, R.color.gray))
            holder.categoryIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray))
        }

        // Click listener for multiple selection
        holder.itemView.setOnClickListener {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position)  // deselect
            } else {
                selectedPositions.add(position)     // select
            }
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = 9

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)
        val categoryText: TextView = itemView.findViewById(R.id.categoryText)
    }
}