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
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.travel.uzoefuapp.BuildConfig
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activities.SelectDestinationActivity
import com.travel.uzoefuapp.discoverDestinationModel.DiscoverDestinationResponse

class DestinationAdapter(
    val context: Context,
    private val discoverList: List<DiscoverDestinationResponse.Datum>,
    private val wishlistClickListener: OnWishlistClickListener,

    ) : RecyclerView.Adapter<DestinationAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.destination_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val destinationList = discoverList[position]

        holder.titleText.text = destinationList.branchName
        holder.activityCount.text = "${destinationList.activityCount.toString()} experiences"

        Glide.with(holder.itemView.context)
            .load(BuildConfig.IMAGE_KEY + destinationList.activityImage)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SelectDestinationActivity::class.java)
            intent.putExtra("categoryName", "${destinationList.branchName} (${destinationList.activityCount})")
            intent.putExtra("branchId", destinationList.branchId.toString())
            context.startActivity(intent)
        }

        holder.favIcon.setImageResource(
            if (destinationList.iswish == true) R.drawable.wishlist_color
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

            wishlistClickListener.onWishlistDestinationClicked(destinationList, position)
        }
    }

    override fun getItemCount(): Int {
        return if (discoverList.size >= 4) 4 else discoverList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleText: TextView = itemView.findViewById(R.id.titleText)
        val activityCount: TextView = itemView.findViewById(R.id.subText)
        val imageView: ImageView = itemView.findViewById(R.id.cardImage)
        val favIcon: ImageView = itemView.findViewById(R.id.favIcon)
    }
}