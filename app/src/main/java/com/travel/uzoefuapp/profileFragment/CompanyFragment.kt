package com.travel.uzoefuapp.profileFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.CategoryAdapter
import com.travel.uzoefuapp.adapter.CompaniesAdapter
import com.travel.uzoefuapp.businessActivities.BusinessProfileActivity
import com.travel.uzoefuapp.databinding.FragmentCompanyBinding
import com.travel.uzoefuapp.databinding.FragmentProfileDetailBinding


class CompanyFragment : Fragment() {
    private var _binding: FragmentCompanyBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCompanyBinding.inflate(inflater, container, false)

        binding.businessProfile.setOnClickListener {
            val intent = Intent(requireContext() , BusinessProfileActivity::class.java)
            startActivity(intent)
        }

        binding.companiesRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding.companiesRecyclerView.adapter = CompaniesAdapter(requireContext())

        return binding.root
    }
}