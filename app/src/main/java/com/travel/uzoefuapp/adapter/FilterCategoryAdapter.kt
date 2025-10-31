package com.travel.uzoefuapp.adapter

import android.content.Context
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

class FilterCategoryAdapter(
    private val context: Context,
    private val categories: List<CategoryResponse.Datum>,
    private val listener: OnCategoryClickListener
) : RecyclerView.Adapter<FilterCategoryAdapter.ViewHolder>() {

    // ✅ Maintain multiple selected IDs as Integers
    private val selectedIds = mutableSetOf<Int>()

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

        val isSelected = selectedIds.contains(category.id)

        // ✅ Update UI based on selection state
        if (isSelected) {
            holder.itemView.setBackgroundResource(R.drawable.category_selected_background)
            holder.categoryText.setTextColor(ContextCompat.getColor(context, R.color.dark_cyan))
            holder.categoryIcon.setColorFilter(ContextCompat.getColor(context, R.color.dark_cyan))
        } else {
            holder.itemView.setBackgroundResource(R.drawable.category_background)
            holder.categoryText.setTextColor(ContextCompat.getColor(context, R.color.gray))
            holder.categoryIcon.setColorFilter(ContextCompat.getColor(context, R.color.gray))
        }

        holder.itemView.setOnClickListener {
            val id = category.id
            if (selectedIds.contains(id)) {
                selectedIds.remove(id)
            } else {
                if (id != null) {
                    selectedIds.add(id)
                }
            }
            notifyItemChanged(position)
            listener.onCategoryClick(getSelectedIds(), getSelectedNames())
        }
    }

    override fun getItemCount(): Int = categories.size

    fun selectAll() {
        selectedIds.clear()
        selectedIds.addAll(categories.mapNotNull { it.id })
        notifyDataSetChanged()
        listener.onCategoryClick(getSelectedIds(), getSelectedNames())
    }

    fun clearAll() {
        selectedIds.clear()
        notifyDataSetChanged()
        listener.onCategoryClick(emptyList(), emptyList())
    }

    fun getSelectedIds(): List<Int> = selectedIds.toList()

    fun getSelectedNames(): List<String> =
        categories.filter { selectedIds.contains(it.id) }
            .mapNotNull { it.name }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryIcon: ImageView = itemView.findViewById(R.id.categoryIcon)
        val categoryText: TextView = itemView.findViewById(R.id.categoryText)
    }

    interface OnCategoryClickListener {
        fun onCategoryClick(selectedIds: List<Int>, selectedNames: List<String>)
    }
}
