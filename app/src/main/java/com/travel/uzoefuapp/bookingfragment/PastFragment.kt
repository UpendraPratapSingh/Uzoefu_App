package com.travel.uzoefuapp.bookingfragment

import CustomProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.adapter.BookingAdapter
import com.travel.uzoefuapp.bookingCompleteModel.BookingCompleteBody
import com.travel.uzoefuapp.bookingCompleteModel.BookingCompleteResponse
import com.travel.uzoefuapp.bookingCompleteModel.BookingCompleteViewModel
import com.travel.uzoefuapp.databinding.FragmentPastBinding
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PastFragment : Fragment() {
    private var _binding: FragmentPastBinding? = null
    private val binding get() = _binding!!
    private val bookingCompleteViewModel: BookingCompleteViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireActivity()) }
    private var bookingList: List<BookingCompleteResponse.Datum> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPastBinding.inflate(inflater, container, false)


        getBookingListApi()
        getBookingListObserver()

        return binding.root
    }

    private fun getBookingListObserver() {
        bookingCompleteViewModel.progressIndicator.observe(viewLifecycleOwner) { isLoading ->
            // Show or hide progress loader here
            if (isLoading == true) {
                // e.g., binding.progressBar.visibility = View.VISIBLE
            } else {
                // e.g., binding.progressBar.visibility = View.GONE
            }
        }

        bookingCompleteViewModel.mCategoryResponse.observe(viewLifecycleOwner) { event ->
            val response = event.peekContent()
            val success = response.success
            val message = response.message
            val data = response.data

            if (success == true && !data.isNullOrEmpty()) {
                bookingList = data
                binding.pastBookingRecyclerView.apply {
                    layoutManager = GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
                    adapter = BookingAdapter(requireContext(), "Past", bookingList)
                }
            } else {
                // Show empty state or message
                // Toast.makeText(requireContext(), message ?: "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

        bookingCompleteViewModel.errorResponse.observe(viewLifecycleOwner) { error ->
            ErrorUtil.handlerGeneralError(requireContext(), error)
        }
    }

    private fun getBookingListApi() {
        val body = BookingCompleteBody(
            status = "completed"
        )
        bookingCompleteViewModel.bookingComplete(progressDialog, requireActivity() , body)
    }
}