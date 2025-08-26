package com.travel.uzoefuapp.productFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
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
}