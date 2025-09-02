package com.travel.uzoefuapp.businessFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.adapter.BusinessActivitiesAdapter
import com.travel.uzoefuapp.businessActivities.AddActivityActivity
import com.travel.uzoefuapp.databinding.FragmentBusinessActivityBinding


class BusinessActivityFragment : Fragment() {
    private var _binding: FragmentBusinessActivityBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBusinessActivityBinding.inflate(inflater, container, false)

        binding.companiesRecyclerView.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)
        binding.companiesRecyclerView.adapter = BusinessActivitiesAdapter(requireContext())

        binding.addMore.setOnClickListener {
            val intent = Intent(requireContext(), AddActivityActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}