package com.travel.uzoefuapp.bookingfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.BookingAdapter
import com.travel.uzoefuapp.databinding.FragmentPastBinding


class PastFragment : Fragment() {
    private var _binding: FragmentPastBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPastBinding.inflate(inflater, container, false)

        binding.pastBookingRecyclerView.layoutManager = GridLayoutManager(requireContext(),1, GridLayoutManager.VERTICAL, false)
        binding.pastBookingRecyclerView.adapter = BookingAdapter(requireContext(), "Past")


        return binding.root
    }
}