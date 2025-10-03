package com.travel.uzoefuapp.bookingDetailFragment

import CustomProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.travel.uzoefuapp.databinding.FragmentStep4Binding
import com.travel.uzoefuapp.paymentModel.PaymentViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Step4Fragment : Fragment() {
    private var _binding: FragmentStep4Binding? = null
    private val binding get() = _binding!!

    private var activityId: String? = null
    private var productName: String? = null
    private val paymentViewModel: PaymentViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }

    companion object {
        fun newInstance(activityId: String, productName: String): Step4Fragment {
            val fragment = Step4Fragment()
            val args = Bundle()
            args.putString("activityId", activityId)
            args.putString("productName", productName)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStep4Binding.inflate(inflater, container, false)

        arguments?.let {
            activityId = it.getString("activityId")
            productName = it.getString("productName")
            saveDataToPrefs()
        }

        binding.activityName.text = productName

        return binding.root
    }

    private fun saveDataToPrefs() {
        val prefs = requireActivity().getSharedPreferences("BookingPrefs", 0)
        prefs.edit().apply {
            putString("activityId", activityId)
            putString("productName", productName)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}