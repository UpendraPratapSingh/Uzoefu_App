package com.travel.uzoefuapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

data class SearchItem(
    val iconRes: Int,
    val title: String,
    val subtitle: String
)

class SearchAdapter(private val items: List<SearchItem>) :
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
        holder.icon.setImageResource(item.iconRes)
        holder.title.text = item.title
        holder.subtitle.text = item.subtitle
    }

    override fun getItemCount() = items.size
}

