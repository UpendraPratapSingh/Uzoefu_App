package com.travel.uzoefuapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.adapter.CategoriesAdapter
import com.travel.uzoefuapp.adapter.DestinationAdapter
import com.travel.uzoefuapp.databinding.FragmentExploreBinding


class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentExploreBinding.inflate(inflater, container, false)

        binding.forYouArrowImg.setOnClickListener {
            parentFragmentManager.popBackStack() // use can back to previous activity
        }

        binding.destinationRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.destinationRecycler.adapter = DestinationAdapter(requireContext())

        binding.categoriesRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.categoriesRecycler.adapter = CategoriesAdapter(requireContext())

        return binding.root

    }
}