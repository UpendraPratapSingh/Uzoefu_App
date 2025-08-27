package com.travel.uzoefuapp.productFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.bookingActivities.BookSummaryActivity
import com.travel.uzoefuapp.databinding.FragmentProductOverviewBinding


class ProductOverviewFragment : Fragment() {
    private var _binding: FragmentProductOverviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProductOverviewBinding.inflate(inflater, container, false)


        return binding.root
    }
}