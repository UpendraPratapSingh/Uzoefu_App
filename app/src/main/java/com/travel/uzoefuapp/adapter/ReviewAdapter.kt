package com.travel.uzoefuapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.application.Uzoefu
import com.travel.uzoefuapp.detailModel.DetailPageResponse
import de.hdodenhof.circleimageview.CircleImageView


class ReviewAdapter(
    private val reviews: List<DetailPageResponse.Data.ActivityRating>,
    private val onEditClick: (DetailPageResponse.Data.ActivityRating) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgUser: CircleImageView = itemView.findViewById(R.id.imgUser)
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvTimeAgo: TextView = itemView.findViewById(R.id.tvTimeAgo)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val tvReviewText: TextView = itemView.findViewById(R.id.tvReviewText)
        val recyclerReviewImages: RecyclerView = itemView.findViewById(R.id.recyclerReviewImages)
        val btnEditReview: ImageView = itemView.findViewById(R.id.btnEditReview)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        val userId = Uzoefu.encryptedPrefs.userId
        val reviewUserId = review.user?.userId.orEmpty()

        holder.tvUserName.text = review.user?.name.toString()
        holder.ratingBar.rating = review.rating!!.toFloat()
        holder.tvReviewText.text = review.description.toString()
        holder.tvTimeAgo.text = review.times.toString()

        val imagePath = "https://uzoefu.co.za/public/uploads/users/"
        review.user?.image?.let { img ->
            Glide.with(holder.itemView.context)
                .load(imagePath + img)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(holder.imgUser)
        }

        if (review.images.isNullOrEmpty()) {
            holder.recyclerReviewImages.visibility = View.GONE
        } else {
            holder.recyclerReviewImages.visibility = View.VISIBLE
            holder.recyclerReviewImages.layoutManager =
                LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.recyclerReviewImages.adapter = ReviewImageAdapter(review.images ?: emptyList())
        }

        holder.btnEditReview.setOnClickListener {
            onEditClick.invoke(review)
        }
    }

    override fun getItemCount() = reviews.size
}

