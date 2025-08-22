package com.travel.uzoefuapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R


class CategoryAdapter(
    private val context: Context,
    private val categories: List<Category>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private val selectedPositions = mutableSetOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]

        holder.categoryText.text = category.name

        holder.categoryIcon.setImageResource(category.iconRes)

        if (selectedPositions.contains(position)) {
            holder.itemView.setBackgroundResource(R.drawable.category_selected_background)
            holder.categoryText.setTextColor(ContextCompat.getColor(context, R.color.dark_cyan))
            holder.categoryIcon.setColorFilter(ContextCompat.getColor(context, R.color.dark_cyan))
        } else {
            holder.itemView.setBackgroundResource(R.drawable.category_background)
            holder.categoryText.setTextColor(ContextCompat.getColor(context, R.color.gray))
            holder.categoryIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray))
        }

        holder.itemView.setOnClickListener {
            if (selectedPositions.contains(position)) {
                selectedPositions.remove(position)
            } else {
                selectedPositions.add(position)
            }
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = categories.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)
        val categoryText: TextView = itemView.findViewById(R.id.categoryText)
    }
}
