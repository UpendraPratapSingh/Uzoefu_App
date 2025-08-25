package com.travel.uzoefuapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.travel.uzoefuapp.R

class SliderAdapter(private val items: List<Any>) : RecyclerView.Adapter<SliderAdapter.SliderVH>() {

    inner class SliderVH(val view: View) : RecyclerView.ViewHolder(view) {
        val img: ImageView = view.findViewById(R.id.imgSlide)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderVH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider_image, parent, false)
        return SliderVH(v)
    }

    override fun onBindViewHolder(holder: SliderVH, position: Int) {
        val item = items[position]
        when (item) {
            is Int -> Glide.with(holder.itemView).load(item).into(holder.img)
            is String -> Glide.with(holder.itemView).load(item).into(holder.img)
        }
    }

    override fun getItemCount() = items.size
}
