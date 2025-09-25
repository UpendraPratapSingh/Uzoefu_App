package com.travel.uzoefuapp.productFragment

import CustomProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.adapter.FAQAdapter
import com.travel.uzoefuapp.databinding.FragmentFAQBinding
import com.travel.uzoefuapp.detailModel.DetailPageBody
import com.travel.uzoefuapp.detailModel.DetailPageResponse
import com.travel.uzoefuapp.detailModel.DetailPageViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FAQFragment : Fragment() {
    private var _binding: FragmentFAQBinding? = null
    private val binding get() = _binding!!
    private var categoryId: Int? = null
    private var faqList: List<DetailPageResponse.Data.Faq> = ArrayList()
    private val detailPageViewModel: DetailPageViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFAQBinding.inflate(inflater, container, false)
        categoryId = arguments?.getInt("categoryId")

        getFaqListApi(categoryId)
        getFaqObserver()


        return binding.root
    }

    private fun getFaqObserver() {
        detailPageViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        detailPageViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            faqList = response.peekContent().data?.faqs ?: emptyList()
            if (success == true) {
                val adapter = FAQAdapter(faqList)
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = adapter
            }
        }
        detailPageViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }

    private fun getFaqListApi(categoryId: Int?) {
        val body = DetailPageBody(
            activity_id = categoryId.toString()
        )
        detailPageViewModel.getDetailPageApi(progressDialog, requireActivity(), body)

    }
}