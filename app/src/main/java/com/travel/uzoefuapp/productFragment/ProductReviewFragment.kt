package com.travel.uzoefuapp.productFragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.Review
import com.travel.uzoefuapp.adapter.ReviewAdapter
import com.travel.uzoefuapp.databinding.FragmentProductReviewBinding


class ProductReviewFragment : Fragment() {
    private var _binding: FragmentProductReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductReviewBinding.inflate(inflater, container, false)

        binding.writeReviewBtn.setOnClickListener {
            showReviewBottomSheet(requireContext())
        }

        val reviews = listOf(
            Review(
                userName = "Rahul Sharma",
                timeAgo = "2 days ago",
                rating = 4.5f,
                reviewText = "Very good product, value for money!",
                userImage = R.drawable.balloon,
                images = listOf(R.drawable.product, R.drawable.birds)
            ),
            Review(
                userName = "Priya Verma",
                timeAgo = "1 week ago",
                rating = 5f,
                reviewText = "Awesome quality and fast delivery.",
                userImage = R.drawable.birds,
                images = listOf(R.drawable.product)
            )
        )

        reviewAdapter = ReviewAdapter(reviews)
        binding.recyclerReviews.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerReviews.adapter = reviewAdapter

        return binding.root
    }

    fun showReviewBottomSheet(context: Context) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(
            R.layout.layout_review_bottom_sheet,
            null
        )

        // Bind Views
        val ratingBar = view.findViewById<RatingBar>(R.id.ratingBar)
        val addPhotosBtn = view.findViewById<Button>(R.id.btnAddPhotos)
        val experienceEdit = view.findViewById<EditText>(R.id.etExperience)
        val postBtn = view.findViewById<Button>(R.id.btnPost)
        val closeBtn = view.findViewById<ImageView>(R.id.ivClose)

        // Close button
        closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        // Add Photos button
        addPhotosBtn.setOnClickListener {
            Toast.makeText(context, "Add photos clicked", Toast.LENGTH_SHORT).show()
            // open gallery / camera code here
        }

        // Post button
        postBtn.setOnClickListener {
            val rating = ratingBar.rating
            val experience = experienceEdit.text.toString().trim()
            Toast.makeText(context, "Posted: $rating stars, $experience", Toast.LENGTH_SHORT).show()
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()
    }

}