package com.travel.uzoefuapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.detailModel.DetailPageResponse

class SliderAdapter(private val images: List<DetailPageResponse.Data.Image>) :
    RecyclerView.Adapter<SliderAdapter.SliderVH>() {

    inner class SliderVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img: ImageView = itemView.findViewById(R.id.imgSlide)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider_image, parent, false)
        return SliderVH(view)
    }

    override fun onBindViewHolder(holder: SliderVH, position: Int) {
        val imagePath =
            "https://uzoefu.co.za/public/images/activity_images/" + images[position].image

        Glide.with(holder.itemView.context)
            .load(imagePath)
            .into(holder.img)
    }

    override fun getItemCount(): Int = images.size
}
