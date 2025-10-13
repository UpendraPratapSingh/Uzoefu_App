package com.travel.uzoefuapp.bookingfragment

import CustomProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.adapter.BookingAdapter
import com.travel.uzoefuapp.adapter.OnBookingActionListener
import com.travel.uzoefuapp.bookingCancelModel.BookingCancelBody
import com.travel.uzoefuapp.bookingCancelModel.BookingCancelViewModel
import com.travel.uzoefuapp.bookingCompleteModel.BookingCompleteBody
import com.travel.uzoefuapp.bookingCompleteModel.BookingCompleteResponse
import com.travel.uzoefuapp.bookingCompleteModel.BookingCompleteViewModel
import com.travel.uzoefuapp.databinding.FragmentActiveBinding
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint
import java.util.ArrayList

@AndroidEntryPoint
class ActiveFragment : Fragment(), OnBookingActionListener {
    private var _binding: FragmentActiveBinding? = null
    private val binding get() = _binding!!
    private val bookingCompleteViewModel: BookingCompleteViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private var bookingList: List<BookingCompleteResponse.Datum> = ArrayList()
    private val bookingCancelViewModel: BookingCancelViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentActiveBinding.inflate(inflater, container, false)

        getBookingListApi()
        getBookingListObserver()
        cancelBookingObserver()

        return binding.root
    }

    private fun cancelBookingObserver() {
        bookingCancelViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        bookingCancelViewModel.bookingCancelResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().status
            val message = response.peekContent().message

            if (success == true) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }

        }
        bookingCancelViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
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
                binding.bookingRecyclerView.apply {
                    layoutManager =
                        GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
                    adapter = BookingAdapter(requireContext(), "Active", bookingList, this@ActiveFragment)
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
            status = "active"
        )
        bookingCompleteViewModel.bookingComplete(progressDialog, requireActivity(), body)
    }

    override fun onCancelBooking(bookingId: String) {
        cancelBookingApi(bookingId)

    }

    private fun cancelBookingApi(bookingId: String) {
        val body = BookingCancelBody(
            bookingId = bookingId
        )
        bookingCancelViewModel.bookingCancelApi(requireActivity(), progressDialog, body)

    }
}