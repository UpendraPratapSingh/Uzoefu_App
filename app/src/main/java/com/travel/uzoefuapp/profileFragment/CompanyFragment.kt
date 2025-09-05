package com.travel.uzoefuapp.profileFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.adapter.CompaniesAdapter
import com.travel.uzoefuapp.databinding.FragmentCompanyBinding


class CompanyFragment : Fragment() {
    private var _binding: FragmentCompanyBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCompanyBinding.inflate(inflater, container, false)

        /*binding.businessProfile.setOnClickListener {
            val intent = Intent(requireContext() , BusinessProfileActivity::class.java)
            intent.putExtra("type", "individual") // 👈 pass data properly
            startActivity(intent)
        }*/

        binding.companiesRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding.companiesRecyclerView.adapter = CompaniesAdapter(requireContext())

        return binding.root
    }
}