package com.travel.uzoefuapp.fragment

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.adapter.WishlistAdapter
import com.travel.uzoefuapp.databinding.FragmentWishlistBinding

class WishlistFragment : Fragment() {
    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: WishlistAdapter
    private var isEditMode = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)

        // Setup RecyclerView
        binding.wishlistRecycler.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)

        adapter = WishlistAdapter(requireContext())
        binding.wishlistRecycler.adapter = adapter

        // Edit button click → Show delete icons
        binding.editText.setOnClickListener {
            val transition = AutoTransition().apply {
                duration = 300
                interpolator = DecelerateInterpolator() // ease out
            }
            TransitionManager.beginDelayedTransition(binding.root, transition)

            isEditMode = true
            adapter.setEditMode(isEditMode)

            binding.editText.visibility = View.GONE
            binding.iconLayout.visibility = View.VISIBLE
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
