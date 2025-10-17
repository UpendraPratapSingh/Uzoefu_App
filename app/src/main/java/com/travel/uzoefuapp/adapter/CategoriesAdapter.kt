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
import com.travel.uzoefuapp.categoryModel.CategoryResponse


class CategoriesAdapter(
    private val context: Context,
    private val categories: List<CategoryResponse.Datum>
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.categories_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]

        holder.bind(category)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SelectDestinationActivity::class.java)
            intent.putExtra("categoryName", "${category.name} (${category.activitiesCount})")
            intent.putExtra("categoryId", category.id.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = categories.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon = itemView.findViewById<ImageView>(R.id.placeIcon)
        private val name = itemView.findViewById<TextView>(R.id.placeTitle)
        private val count = itemView.findViewById<TextView>(R.id.placeCount)

        fun bind(category: CategoryResponse.Datum) {
            val baseImagePath = "https://uzoefu.co.za/public/icons/"

            Glide.with(itemView.context)
                .load(baseImagePath + category.icon)
                .placeholder(R.drawable.wellness)
                .into(icon)

            name.text = category.name
            count.text = "(${category.activitiesCount})"
        }
    }
}
