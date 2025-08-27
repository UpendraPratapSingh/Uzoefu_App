package com.travel.uzoefuapp.fragment

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.CategoriesAdapter
import com.travel.uzoefuapp.adapter.Category
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

        binding.destinationRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.destinationRecycler.adapter = DestinationAdapter(requireContext())

        val categoriesList = listOf(
            Category("Pretoria", 64, R.drawable.tours),
            Category("Drakensberg", 56, R.drawable.tours),
            Category("Victoria", 54, R.drawable.tours),
            Category("Kimberly", 46, R.drawable.tours),
            Category("Brits", 30, R.drawable.tours),
            Category("Polokwane", 18, R.drawable.tours),
            Category("Hout Bay", 250, R.drawable.tours),
            Category("New Castle", 66, R.drawable.tours),
            Category("Njelele", 131, R.drawable.tours),
            Category("Wildlife", 65, R.drawable.tours),
            Category("Sabie", 50, R.drawable.tours),
        )

        binding.categoriesRecycler.layoutManager =
            GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
        binding.categoriesRecycler.adapter = CategoriesAdapter(requireContext(), categoriesList)

        return binding.root

    }
}