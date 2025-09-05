package com.travel.uzoefuapp.businessFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.uzoefuapp.businessActivities.CompanyDetailActivity
import com.travel.uzoefuapp.businessActivities.ContactActivity
import com.travel.uzoefuapp.businessActivities.OperatingHoursActivity
import com.travel.uzoefuapp.businessActivities.PaymentDetailActivity
import com.travel.uzoefuapp.databinding.FragmentBusinessProfileBinding

class BusinessProfileFragment : Fragment() {
    private var _binding: FragmentBusinessProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBusinessProfileBinding.inflate(inflater, container, false)

        binding.tvCompanyDetails.setOnClickListener {
            val intent = Intent(requireContext(), CompanyDetailActivity::class.java)
            startActivity(intent)
        }

        binding.tvContact.setOnClickListener {
            val intent = Intent(requireContext(), ContactActivity::class.java)
            startActivity(intent)
        }

        binding.tvOperatingHours.setOnClickListener {
            val intent = Intent(requireContext(), OperatingHoursActivity::class.java)
            startActivity(intent)
        }

        binding.tvPaymentInfo.setOnClickListener {
            val intent = Intent(requireContext(), PaymentDetailActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}