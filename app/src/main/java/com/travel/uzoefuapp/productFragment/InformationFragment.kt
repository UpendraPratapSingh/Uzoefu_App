package com.travel.uzoefuapp.productFragment

import CustomProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.adapter.BusinessHour
import com.travel.uzoefuapp.adapter.ExpandableItem
import com.travel.uzoefuapp.adapter.FAQAdapter
import com.travel.uzoefuapp.adapter.InformationAdapter
import com.travel.uzoefuapp.databinding.FragmentInformationBinding
import com.travel.uzoefuapp.detailModel.DetailPageBody
import com.travel.uzoefuapp.detailModel.DetailPageResponse
import com.travel.uzoefuapp.detailModel.DetailPageViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InformationFragment : Fragment() {
    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!
    private var categoryId: Int? = null
    private val detailPageViewModel: DetailPageViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private var informationList: List<DetailPageResponse.Data.Hours> = ArrayList()
    private var indemnityAnswer: String = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInformationBinding.inflate(inflater, container, false)
        categoryId = arguments?.getInt("categoryId")

        setupObserver()
        getDetailApi()

        return binding.root
    }

    private fun getDetailApi() {
        val body = DetailPageBody(
            activity_id = categoryId.toString()
        )
        detailPageViewModel.getDetailPageApi(progressDialog, requireActivity(), body)

    }

    private fun setupObserver() {
        detailPageViewModel.progressIndicator.observe(viewLifecycleOwner) {}

        detailPageViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message

            if (success == true) {
                val hoursData = response.peekContent().data?.hours

                // Convert API Hours into ExpandableItem
                val expandableList = mutableListOf<ExpandableItem>()

                hoursData?.let { apiHours ->
                    val businessHours = mutableListOf<BusinessHour>()
                    apiHours.monFrom?.let {
                        businessHours.add(
                            BusinessHour(
                                "Monday",
                                "${apiHours.monFrom} - ${apiHours.monTo}"
                            )
                        )
                    }
                    apiHours.tueFrom?.let {
                        businessHours.add(
                            BusinessHour(
                                "Tuesday",
                                "${apiHours.tueFrom} - ${apiHours.tueTo}"
                            )
                        )
                    }
                    apiHours.wedFrom?.let {
                        businessHours.add(
                            BusinessHour(
                                "Wednesday",
                                "${apiHours.wedFrom} - ${apiHours.wedTo}"
                            )
                        )
                    }
                    apiHours.thuFrom?.let {
                        businessHours.add(
                            BusinessHour(
                                "Thursday",
                                "${apiHours.thuFrom} - ${apiHours.thuTo}"
                            )
                        )
                    }
                    apiHours.friFrom?.let {
                        businessHours.add(
                            BusinessHour(
                                "Friday",
                                "${apiHours.friFrom} - ${apiHours.friTo}"
                            )
                        )
                    }
                    apiHours.satFrom?.let {
                        businessHours.add(
                            BusinessHour(
                                "Saturday",
                                "${apiHours.satFrom} - ${apiHours.satTo}"
                            )
                        )
                    }
                    apiHours.sunFrom?.let {
                        businessHours.add(
                            BusinessHour(
                                "Sunday",
                                "${apiHours.sunFrom} - ${apiHours.sunTo}"
                            )
                        )
                    }
                    apiHours.publicMonFrom?.let {
                        businessHours.add(
                            BusinessHour(
                                "Public Holiday Hours",
                                "${apiHours.publicMonFrom} - ${apiHours.publicMonTo}"
                            )
                        )
                    }

                    expandableList.add(
                        ExpandableItem("Activity Hours", hours = businessHours)
                    )
                }

                expandableList.add(
                    ExpandableItem(
                        "Amenities",
                        answer = response.peekContent().data?.amenities
                            ?.joinToString("\n") { it.amenity?.name.toString() } ?: ""
                    )
                )

                expandableList.add(
                    ExpandableItem(
                        "Terms & Conditions",
                        answer = (response.peekContent().data?.terms?.termsAndConditions
                            ?: "").toString()
                    )
                )
                // 1️⃣ Combine all Indemnity fields
                val indemnityAnswer = listOfNotNull(
                    response.peekContent().data?.indemnity?.agreement,
                    response.peekContent().data?.indemnity?.waiverAndIndemnity,
                    response.peekContent().data?.indemnity?.declaration,
                    response.peekContent().data?.indemnity?.acknowledgement
                ).joinToString(separator = "\n") { "• $it" }

               // 2️⃣ Add to expandable list
                expandableList.add(
                    ExpandableItem(
                        title = "Indemnity",
                        answer = indemnityAnswer
                    )
                )

                val adapter = InformationAdapter(expandableList)
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerView.adapter = adapter
            }
        }

        detailPageViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }
}