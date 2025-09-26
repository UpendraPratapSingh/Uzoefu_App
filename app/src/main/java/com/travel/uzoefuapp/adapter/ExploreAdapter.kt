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
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.activityModl.ActivityResponse
import com.travel.uzoefuapp.companyActivities.BookingProductActivity

class ExploreAdapter(
    val context: Context,
    private val activityList: List<ActivityResponse.Datum>,
    private val wishlistClickListener: OnWishlistListener,
) : RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.explore_recyclerview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val list = activityList[position]

        val imagePath = "https://mobappssolutions.in/uzoefu/public/images/activity_images/"

        holder.titleText.text = list.name
        holder.timing.text = list.todayHours
        holder.rating.text = "${list.rating} (${list.ratingCount})"
        holder.price.text = "R ${list.activityPrice.toString()}"

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(imagePath + list.image)
            .diskCacheStrategy(DiskCacheStrategy.ALL) // cache both original and transformed images
            .into(holder.image)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, BookingProductActivity::class.java)
            intent.putExtra("categoryId", list.id)
            context.startActivity(intent)
        }

        holder.favIcon.setImageResource(
            if (list.isWish == true) R.drawable.wishlist_color
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

            wishlistClickListener.onWishlistClick(list, position)
        }

    }

    override fun getItemCount(): Int {
        return activityList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val image = itemView.findViewById<ImageView>(R.id.forYouCountryImg)
        val titleText = itemView.findViewById<TextView>(R.id.forYouDestName)
        val timing = itemView.findViewById<TextView>(R.id.noOfDays)
        val rating = itemView.findViewById<TextView>(R.id.priceTxt)
        val price = itemView.findViewById<TextView>(R.id.priceTxt1)
        val favIcon = itemView.findViewById<ImageView>(R.id.favIcon)
    }
}