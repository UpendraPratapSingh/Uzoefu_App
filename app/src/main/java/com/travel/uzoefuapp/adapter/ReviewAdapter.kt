package com.travel.uzoefuapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import de.hdodenhof.circleimageview.CircleImageView

data class Review(
    val userName: String,
    val timeAgo: String,
    val rating: Float,
    val reviewText: String,
    val userImage: Int,
    val images: List<Int>
)

class ReviewAdapter(private val reviews: List<Review>) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgUser = itemView.findViewById<CircleImageView>(R.id.imgUser)
        val tvUserName = itemView.findViewById<TextView>(R.id.tvUserName)
        val tvTimeAgo = itemView.findViewById<TextView>(R.id.tvTimeAgo)
        val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
        val tvReviewText = itemView.findViewById<TextView>(R.id.tvReviewText)
        val recyclerReviewImages = itemView.findViewById<RecyclerView>(R.id.recyclerReviewImages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.tvUserName.text = review.userName
        holder.tvTimeAgo.text = review.timeAgo
        holder.ratingBar.rating = review.rating
        holder.tvReviewText.text = review.reviewText
        holder.imgUser.setImageResource(review.userImage)

        holder.recyclerReviewImages.layoutManager =
            LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
        holder.recyclerReviewImages.adapter = ReviewImageAdapter(review.images)
    }

    override fun getItemCount() = reviews.size
}
