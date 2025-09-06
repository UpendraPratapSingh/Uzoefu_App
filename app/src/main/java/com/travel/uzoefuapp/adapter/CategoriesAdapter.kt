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

data class Category(
    val name: String,
    val count: Int,
    val iconRes: Int
)


class CategoriesAdapter(
    private val context: Context,
    private val categories: List<Category>
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
            intent.putExtra("categoryName", "${category.name} (${category.count})")

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = categories.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val icon = itemView.findViewById<ImageView>(R.id.placeIcon)
        private val name = itemView.findViewById<TextView>(R.id.placeTitle)
        private val count = itemView.findViewById<TextView>(R.id.placeCount)

        fun bind(category: Category) {
            icon.setImageResource(category.iconRes)
            name.text = category.name
            count.text = "(${category.count})"
        }
    }
}
