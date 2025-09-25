package com.travel.uzoefuapp.bookingDetailFragment

import CustomProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.travel.uzoefuapp.databinding.FragmentStep2Binding
import com.travel.uzoefuapp.getProfileModel.GetProfileViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Step2Fragment : Fragment() {
    private var _binding: FragmentStep2Binding? = null
    private val binding get() = _binding!!
    private val getProfileViewModel: GetProfileViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStep2Binding.inflate(inflater, container, false)

        getProfileApi()
        getProfileObserver()

        return binding.root
    }
    private fun getProfileObserver() {
        getProfileViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        getProfileViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data
            if (success == true) {
                binding.firstName.setText(data?.name.toString())
                binding.lastName.setText(data?.lastname.toString())
                binding.etUsername.setText(data?.username.toString())
                binding.mobileNumber.setText(data?.mobile.toString())
                binding.billingAddress.setText(data?.city.toString())

            }

        }
        getProfileViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getProfileApi() {
        getProfileViewModel.getProfileApi(progressDialog, requireActivity())

    }
}