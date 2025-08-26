package com.travel.uzoefuapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.travel.uzoefuapp.R

class ProductSliderAdapter(
    private val images: List<Int>,
    private val onThumbnailClick: (position: Int) -> Unit
) : RecyclerView.Adapter<ProductSliderAdapter.ThumbViewHolder>() {

    inner class ThumbViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val thumbImage: ImageView = itemView.findViewById(R.id.thumbImage)
        val overlay: FrameLayout = itemView.findViewById(R.id.overlayLayout)
        val overlayText: TextView = itemView.findViewById(R.id.overlayText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ThumbViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_slider_recyclerview, parent, false)
        return ThumbViewHolder(view)
    }

    override fun onBindViewHolder(holder: ThumbViewHolder, position: Int) {
        val totalImages = images.size

        if (position < 4) {
            Glide.with(holder.thumbImage.context)
                .load(images[position])
                .into(holder.thumbImage)

            holder.overlay.visibility = View.GONE

            holder.itemView.setOnClickListener { onThumbnailClick(position) }
        } else if (position == 4) {
            Glide.with(holder.thumbImage.context)
                .load(images[position])
                .into(holder.thumbImage)

            holder.overlay.visibility = View.VISIBLE
            holder.overlayText.text = "+${totalImages}"

            holder.itemView.setOnClickListener { onThumbnailClick(position) }
        }
    }

    override fun getItemCount(): Int {
        return if (images.size > 5) 5 else images.size
    }
}