package com.travel.uzoefuapp.profileFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.adapter.CategoryAdapter
import com.travel.uzoefuapp.adapter.FavouriteAdapter
import com.travel.uzoefuapp.databinding.FragmentProfileDetailBinding

class ProfileDetailFragment : Fragment() {

    // ✅ ViewBinding instance
    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Spinner data
        val distanceRanges = listOf("1 km", "5 km", "10 km", "20 km", "50 km")

        // Setup adapter
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            distanceRanges
        )

        // ✅ Bind AutoCompleteTextView
        binding.spinnerDistanceRange.setAdapter(adapter)

        // ✅ Setup RecyclerView
        binding.categoriesRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.adapter = FavouriteAdapter(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // ✅ Prevent memory leaks
    }
}
