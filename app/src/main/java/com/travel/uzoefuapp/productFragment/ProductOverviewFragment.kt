package com.travel.uzoefuapp.productFragment

import CustomProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
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
        // Inflate the layout for this fragment
        _binding = FragmentProductOverviewBinding.inflate(inflater, container, false)
        categoryId = arguments?.getInt("categoryId")
        activeHour = arguments?.getString("activeHour").toString()

        Log.e("TAG", "onCreateView: $activeHour", )

        categoryId?.let { getDetailApi(it) }
        getDetailObserver()

        return binding.root
    }

    private fun getDetailObserver() {
        detailPageViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        detailPageViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data1 = response.peekContent().data?.description
            val data2 = response.peekContent().data?.ActivityRating()
            val data3 = response.peekContent().data?.activity?.branch
            val data4 = response.peekContent().data
            //val description = data1?.highlights.toString().removeSurrounding("[", "]")
            //val description1 = data1?.highlights?.joinToString(separator = "\n") ?: ""

            if (success == true) {
                binding.tvDescription.text = data1?.description ?: ""
                binding.highlights.text = data1?.highlights?.joinToString("\n") { "• $it" } ?: ""
                binding.tvLocation.text = "${data3?.address.toString()} , ${data3?.town.toString()}"
                binding.tvPhone.text = "Tel: +${data3?.teliphoneNumber.toString()}"
                binding.tvCell.text = "Cel: +${data3?.contactNumber.toString()}"
                binding.tvTime.text = activeHour
                val ratingValue = 1.5f  // static rating
                binding.ratingBar.rating = ratingValue
                binding.tvRating.text = ratingValue.toString()
            }
        }
        detailPageViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getDetailApi(categoryId: Int) {
        val body = DetailPageBody(
            activity_id = categoryId.toString()
        )
        detailPageViewModel.getDetailPageApi(progressDialog, requireActivity(), body)

    }

}