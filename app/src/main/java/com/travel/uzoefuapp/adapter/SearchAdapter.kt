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
import com.travel.uzoefuapp.SearchActivityModel.SearchActivityResponse
import com.travel.uzoefuapp.companyActivities.BookingProductActivity

class SearchAdapter(
    val context: Context,
    private val items: MutableList<SearchActivityResponse.Datum>
) :
    RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {

    inner class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.iconImage)
        val title: TextView = view.findViewById(R.id.titleText)
        val subtitle: TextView = view.findViewById(R.id.subtitleText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_search_result, parent, false)
        return SearchViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.name
        holder.subtitle.text = item.categoryName

        holder.itemView.setOnClickListener {
            val intent = Intent(context, BookingProductActivity::class.java)
            intent.putExtra("categoryId", item.activityId)
            context.startActivity(intent)
        }

    }

    override fun getItemCount() = items.size

    fun updateData(newList: List<SearchActivityResponse.Datum>) {
        items.clear()
        items.addAll(newList)
        notifyDataSetChanged()
    }
}
