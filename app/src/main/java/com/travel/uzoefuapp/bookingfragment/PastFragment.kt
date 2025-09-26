package com.travel.uzoefuapp.bookingfragment

import CustomProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.BookingAdapter
import com.travel.uzoefuapp.bookingCompleteModel.BookingCompleteResponse
import com.travel.uzoefuapp.bookingCompleteModel.BookingCompleteViewModel
import com.travel.uzoefuapp.databinding.FragmentPastBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PastFragment : Fragment() {
    private var _binding: FragmentPastBinding? = null
    private val binding get() = _binding!!
    private val bookingCompleteViewModel: BookingCompleteViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }
    private val bookingList: List<BookingCompleteResponse.Datum> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPastBinding.inflate(inflater, container, false)

        binding.pastBookingRecyclerView.layoutManager = GridLayoutManager(requireContext(),1, GridLayoutManager.VERTICAL, false)
        binding.pastBookingRecyclerView.adapter = BookingAdapter(requireContext(), "Past", bookingList)

        return binding.root
    }
}