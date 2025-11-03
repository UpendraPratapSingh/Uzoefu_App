package com.travel.uzoefuapp.profileFragment

import CustomProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.RatingReviewAdapter
import com.travel.uzoefuapp.databinding.FragmentReviewsBinding
import com.travel.uzoefuapp.ratingReviewModel.RatingReviewResponse
import com.travel.uzoefuapp.ratingReviewModel.RatingReviewViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewsFragment : Fragment() {
    private var _binding: FragmentReviewsBinding? = null
    private val binding get() = _binding!!
    private val ratingReviewViewModel: RatingReviewViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }
    private var ratingList: List<RatingReviewResponse.Data.Datum> = ArrayList()
    private lateinit var reviewAdapter: RatingReviewAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentReviewsBinding.inflate(inflater, container, false)

        ratingReviewApi()
        ratingReviewObserver()

        return binding.root
    }

    private fun ratingReviewObserver() {
        ratingReviewViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        ratingReviewViewModel.ratingReviewResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            ratingList = response.peekContent().data?.data ?: emptyList()

            if (success == true) {
                recyclerView = view?.findViewById(R.id.rating_review_recycler_view)!!
                reviewAdapter = RatingReviewAdapter(ratingList)

                recyclerView.layoutManager = LinearLayoutManager(requireContext())
                recyclerView.adapter = reviewAdapter
            }
        }
        ratingReviewViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun ratingReviewApi() {
        ratingReviewViewModel.ratingReviewApi(requireActivity(), progressDialog)

    }
}