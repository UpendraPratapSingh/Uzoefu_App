package com.travel.uzoefuapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

class ExploreAdapter(val context: Context) : RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int, ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.explore_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return 6
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) { }
}