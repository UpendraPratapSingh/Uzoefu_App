package com.travel.uzoefuapp.productFragment

import CustomProgressDialog
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.FragmentProductOverviewBinding
import com.travel.uzoefuapp.detailModel.DetailPageBody
import com.travel.uzoefuapp.detailModel.DetailPageViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProductOverviewFragment : Fragment() {
    private var _binding: FragmentProductOverviewBinding? = null
    private val binding get() = _binding!!
    private var categoryId: Int? = null
    private var activeHour = ""
    private val detailPageViewModel: DetailPageViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProductOverviewBinding.inflate(inflater, container, false)
        categoryId = arguments?.getInt("categoryId")
        activeHour = arguments?.getString("activeHour").toString()

        categoryId?.let { getDetailApi(it) }
        getDetailObserver()

        return binding.root

    }

    private fun getDetailObserver() {
        detailPageViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        detailPageViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val data1 = response.peekContent().data?.description
            val data2 = response.peekContent().data?.activityRating
            val data3 = response.peekContent().data?.activity?.branch
            val data4 = response.peekContent().data?.todayHours
            val ratingCount = response.peekContent().data?.ratingCount
            val stateName = data3?.state?.name
            
            if (success == true) {
                binding.tvDescription.text = data1?.description ?: ""
                binding.highlights.text = data1?.highlights?.joinToString("\n") { "• $it" } ?: ""
                binding.tvLocation.text =
                    "${data3?.branchName.toString()} , ${stateName.toString()}"
                binding.tvPhone.text = "Tel:+${data3?.teliphoneNumber.toString()}"
                binding.tvCell.text = "Cel:+${data3?.contactNumber.toString()}"
                binding.tvTime.text = data4.toString()
                val ratings = data2?.mapNotNull { it.rating?.toFloat() } ?: emptyList()
                val averageRating = if (ratings.isNotEmpty()) ratings.average().toFloat() else 0f

                binding.ratingBar.rating = averageRating
                val ratingText = String.format("%.1f (%d)", averageRating, ratingCount)
                val spannable = SpannableString(ratingText)

                val startIndex = ratingText.indexOf("(")
                val endIndex = ratingText.indexOf(")") + 1

                spannable.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(requireContext(), R.color.green_color)
                    ),
                    startIndex,
                    endIndex,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                binding.tvRating.text = spannable
            }
        }
        detailPageViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getDetailApi(categoryId: Int) {
        val body = DetailPageBody(activity_id = categoryId.toString())
        detailPageViewModel.getDetailPageApi(progressDialog, requireActivity(), body)
    }
}