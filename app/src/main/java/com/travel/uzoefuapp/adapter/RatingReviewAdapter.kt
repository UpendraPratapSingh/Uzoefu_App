package com.travel.uzoefuapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.ratingReviewModel.RatingReviewResponse

class RatingReviewAdapter(private val reviews: List<RatingReviewResponse.Data.Datum>) :
    RecyclerView.Adapter<RatingReviewAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)
        val ratingBar: AppCompatRatingBar = itemView.findViewById(R.id.ratingBar)
        val tvTimeAgo: TextView = itemView.findViewById(R.id.tvTimeAgo)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val rvImages: RecyclerView = itemView.findViewById(R.id.rvImages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.rating_review_layout, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]

        holder.tvTitle.text = review.activityName.toString()
        holder.tvLocation.text = (review.stateName ?: "").toString()
        holder.ratingBar.rating = review.rating!!.toFloat()
        holder.tvTimeAgo.text = review.timeAgo
        holder.tvDescription.text = review.description

        if (review.images?.isNotEmpty() == true) {
            holder.rvImages.visibility = View.VISIBLE
            holder.rvImages.layoutManager =
                LinearLayoutManager(holder.itemView.context, LinearLayoutManager.HORIZONTAL, false)
            holder.rvImages.adapter = ImageAdapter(review.images ?: emptyList())
        } else {
            holder.rvImages.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = reviews.size

}