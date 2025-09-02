package com.travel.uzoefuapp.businessFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.uzoefuapp.databinding.FragmentBusinessBookingBinding


class BusinessBookingFragment : Fragment() {
    private var _binding: FragmentBusinessBookingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBusinessBookingBinding.inflate(inflater, container, false)
        return binding.root
    }
}