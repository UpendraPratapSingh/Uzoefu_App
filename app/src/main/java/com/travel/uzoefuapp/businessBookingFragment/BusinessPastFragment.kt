package com.travel.uzoefuapp.businessBookingFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.adapter.BusinessBookingAdapter
import com.travel.uzoefuapp.databinding.FragmentBusinessPastBinding


class BusinessPastFragment : Fragment() {
    private var _binding: FragmentBusinessPastBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBusinessPastBinding.inflate(inflater, container, false)

        binding.bookingRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding.bookingRecyclerView.adapter = BusinessBookingAdapter(requireContext(), "")

        return binding.root
    }
}