package com.travel.uzoefuapp.businessFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.uzoefuapp.databinding.FragmentBusinessOverviewBinding


class BusinessOverviewFragment : Fragment() {
    private var _binding: FragmentBusinessOverviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBusinessOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }
}