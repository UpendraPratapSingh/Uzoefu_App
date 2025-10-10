package com.travel.uzoefuapp.bookingDetailFragment

import CustomProgressDialog
import android.content.Context
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
import androidx.core.widget.addTextChangedListener

@AndroidEntryPoint
class Step2Fragment : Fragment() {
    private var _binding: FragmentStep2Binding? = null
    private val binding get() = _binding!!
    private val getProfileViewModel: GetProfileViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    private val sharedPref by lazy {
        requireContext().getSharedPreferences("profile_pref", Context.MODE_PRIVATE)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep2Binding.inflate(inflater, container, false)

        getProfileApi()
        getProfileObserver()
        setupManualSave()

        return binding.root
    }

    private fun getProfileObserver() {
        getProfileViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        getProfileViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val data = response.peekContent().data
            if (success == true && data != null) {
                binding.firstName.setText(data.name ?: "")
                binding.lastName.setText(data.lastname ?: "")
                binding.etUsername.setText(data.username ?: "")
                binding.mobileNumber.setText(data.mobile ?: "")
                binding.billingAddress.setText(data.city ?: "")

                saveToPreferences(
                    firstName = data.name ?: "",
                    lastName = data.lastname ?: "",
                    username = data.username ?: "",
                    mobile = data.mobile ?: "",
                    billingAddress = data.city ?: ""
                )
            }
        }
        getProfileViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getProfileApi() {
        getProfileViewModel.getProfileApi(progressDialog, requireActivity())
    }

    // Save user-typed input immediately
    private fun setupManualSave() {
        binding.firstName.addTextChangedListener { saveCurrentInput() }
        binding.lastName.addTextChangedListener { saveCurrentInput() }
        binding.etUsername.addTextChangedListener { saveCurrentInput() }
        binding.mobileNumber.addTextChangedListener { saveCurrentInput() }
        binding.billingAddress.addTextChangedListener { saveCurrentInput() }
    }

    private fun saveCurrentInput() {
        saveToPreferences(
            firstName = binding.firstName.text.toString(),
            lastName = binding.lastName.text.toString(),
            username = binding.etUsername.text.toString(),
            mobile = binding.mobileNumber.text.toString(),
            billingAddress = binding.billingAddress.text.toString()
        )
    }

    private fun saveToPreferences(
        firstName: String,
        lastName: String,
        username: String,
        mobile: String,
        billingAddress: String
    ) {
        with(sharedPref.edit()) {
            putString("first_name", firstName)
            putString("last_name", lastName)
            putString("username", username)
            putString("mobile", mobile)
            putString("billing_address", billingAddress)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
