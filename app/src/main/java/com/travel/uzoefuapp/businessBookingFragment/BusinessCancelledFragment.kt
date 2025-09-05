package com.travel.uzoefuapp.businessBookingFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.BookingAdapter
import com.travel.uzoefuapp.adapter.BusinessBookingAdapter
import com.travel.uzoefuapp.databinding.FragmentBusinessCancelledBinding


class BusinessCancelledFragment : Fragment() {
    private var _binding: FragmentBusinessCancelledBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBusinessCancelledBinding.inflate(inflater, container, false)

        binding.bookingRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding.bookingRecyclerView.adapter = BusinessBookingAdapter(requireContext(), "Cancelled")

        return binding.root
    }
}