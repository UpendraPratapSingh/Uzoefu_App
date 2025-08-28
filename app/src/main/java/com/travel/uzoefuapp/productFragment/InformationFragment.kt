package com.travel.uzoefuapp.productFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.adapter.BusinessHour
import com.travel.uzoefuapp.adapter.ExpandableItem
import com.travel.uzoefuapp.adapter.FAQAdapter
import com.travel.uzoefuapp.adapter.InformationAdapter
import com.travel.uzoefuapp.databinding.FragmentInformationBinding


class InformationFragment : Fragment() {
    private var _binding: FragmentInformationBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentInformationBinding.inflate(inflater, container, false)

        val list = listOf(
            ExpandableItem(
                "Activity Hours",
                hours = listOf(
                    BusinessHour("Monday", "09:00 - 17:00"),
                    BusinessHour("Tuesday", "09:00 - 17:00"),
                    BusinessHour("Wednesday", "09:00 - 17:00"),
                    BusinessHour("Thursday", "09:00 - 17:00"),
                    BusinessHour("Friday", "08:00 - 12:00"),
                    BusinessHour("Saturday", "09:00 - 17:00"),
                    BusinessHour("Sunday", "09:00 - 17:00"),
                ),
                content = "Public Holiday Hours\n08:00 - 12:00"
            ),
            ExpandableItem("Amenities", content = "List of amenities goes here..."),
            ExpandableItem("Terms & Conditions", content = "Terms & conditions text goes here..."),
            ExpandableItem("Indemnity", content = "Indemnity information goes here...")
        )

        val adapter = InformationAdapter(list, requireContext())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter


        return binding.root
    }
}