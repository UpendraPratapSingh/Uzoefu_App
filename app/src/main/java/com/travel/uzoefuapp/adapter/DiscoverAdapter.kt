package com.travel.uzoefuapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.travel.uzoefuapp.BuildConfig
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.discoverDestinationModel.DiscoverDestinationResponse


class DiscoverAdapter(
    val context: Context,
    val destinationList: List<DiscoverDestinationResponse.Datum>,
    private val wishlistClickListener: OnWishlistClickListener,

    ) : RecyclerView.Adapter<DiscoverAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.discover_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val discoverList = destinationList[position]

        holder.titleText.text = discoverList.branchName
        holder.activityCount.text = "(${discoverList.activityCount.toString()} Activities)"

        Glide.with(holder.itemView.context)
            .load(BuildConfig.IMAGE_KEY + discoverList.activityImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.backImage)

        holder.favIcon.setImageResource(
            if (discoverList.iswish == true) R.drawable.wishlist_color
            else R.drawable.ic_wish
        )

        holder.favIcon.setOnClickListener { view ->
            view.animate()
                .scaleX(1.3f)
                .scaleY(1.3f)
                .setDuration(150)
                .withEndAction {
                    view.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(150)
                        .start()
                }
                .start()

            wishlistClickListener.onWishlistDestinationClicked(discoverList, position)
        }
    }

    override fun getItemCount(): Int {
        return destinationList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val activityCount: TextView = itemView.findViewById(R.id.subText)
        val backImage: ImageView = itemView.findViewById(R.id.cardImage)
        val favIcon: ImageView = itemView.findViewById(R.id.favIcon)

    }
}