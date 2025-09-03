package com.travel.uzoefuapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R

class AmenitiesAdapter(private val amenities: List<String>) :
    RecyclerView.Adapter<AmenitiesAdapter.AmenityViewHolder>() {

    inner class AmenityViewHolder(val checkBox: CheckBox) :
        RecyclerView.ViewHolder(checkBox)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmenityViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_amenity, parent, false) as CheckBox
        return AmenityViewHolder(view)
    }

    override fun onBindViewHolder(holder: AmenityViewHolder, position: Int) {
        holder.checkBox.text = amenities[position]
    }

    override fun getItemCount(): Int = amenities.size
}
