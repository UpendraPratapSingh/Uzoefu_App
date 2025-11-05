package com.travel.uzoefuapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.categoryModel.CategoryResponse

class CategoryAdapter(
    private val context: Context,
    private val categories: List<CategoryResponse.Datum>,
    private val listener: OnCategoryClickListener,
    private var selectedId: String = ""

) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var selectedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        holder.categoryText.text = category.name

        val baseImagePath = "https://uzoefu.co.za/public/icons/"
        Glide.with(context)
            .load(baseImagePath + category.icon)
            .placeholder(R.drawable.wellness)
            .into(holder.categoryIcon)

        Log.e("SelectedId", "selectedId: $selectedId")
        Log.e("SelectedId", "categoryId: ${category.id}")

        if (category.id.toString() == selectedId) {
            holder.itemView.setBackgroundResource(R.drawable.category_selected_background)
            holder.categoryText.setTextColor(ContextCompat.getColor(context, R.color.dark_cyan))
            holder.categoryIcon.setColorFilter(ContextCompat.getColor(context, R.color.dark_cyan))
            listener.onCategoryClick(category.id.toString(), category.name ?: "")

        } else {
            holder.itemView.setBackgroundResource(R.drawable.category_background)
            holder.categoryText.setTextColor(ContextCompat.getColor(context, R.color.gray))
            holder.categoryIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray))
        }

        holder.itemView.setOnClickListener {
            selectedId = category.id.toString()
            notifyDataSetChanged()
            listener.onCategoryClick(category.id.toString(), category.name ?: "")
        }
    }

    override fun getItemCount(): Int = categories.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)
        val categoryText: TextView = itemView.findViewById(R.id.categoryText)
    }
}
