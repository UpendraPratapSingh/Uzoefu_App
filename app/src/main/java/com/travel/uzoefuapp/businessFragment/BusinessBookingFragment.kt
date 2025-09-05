package com.travel.uzoefuapp.businessFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import com.travel.uzoefuapp.adapter.BookingTabAdapter
import com.travel.uzoefuapp.adapter.BusinessBookingTabAdapter
import com.travel.uzoefuapp.databinding.FragmentBusinessBookingBinding


class BusinessBookingFragment : Fragment() {
    private var _binding: FragmentBusinessBookingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBusinessBookingBinding.inflate(inflater, container, false)


        val adapter = BusinessBookingTabAdapter(this)
        binding.viewPager.adapter = adapter


        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Active"
                1 -> "Past"
                2 -> "Cancelled"
                else -> ""
            }
        }.attach()

        return binding.root
    }
}