package com.travel.uzoefuapp.fragment

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.travel.uzoefuapp.R
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

        binding.wishlistRecycler.layoutManager =
            GridLayoutManager(requireContext(), 1, GridLayoutManager.VERTICAL, false)

        adapter = WishlistAdapter(requireContext())
        binding.wishlistRecycler.adapter = adapter

        binding.editText.setOnClickListener {
            val transition = AutoTransition().apply {
                duration = 300
                interpolator = DecelerateInterpolator()
            }
            TransitionManager.beginDelayedTransition(binding.root, transition)

            isEditMode = true
            adapter.setEditMode(isEditMode)

            binding.editText.visibility = View.GONE
            binding.iconLayout.visibility = View.VISIBLE
        }

        binding.copyIcon.setOnClickListener { openBottomSheetTrip() }

        return binding.root
    }

    private fun openBottomSheetTrip() {
        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(view)

        val closeBtn = view.findViewById<ImageView>(R.id.tvCloseBtn)
        val createNewTrip = view.findViewById<LinearLayout>(R.id.createNewTrip)

        closeBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        createNewTrip.setOnClickListener {
            val bottomSheetDialog = BottomSheetDialog(requireContext())
            val view = layoutInflater.inflate(R.layout.bottom_sheet_create_trip, null)
            bottomSheetDialog.setContentView(view)

            val closeBtn = view.findViewById<ImageView>(R.id.tvCloseBtn)
            val etTripTitle = view.findViewById<EditText>(R.id.etTripTitle)
            val etTripDestination = view.findViewById<EditText>(R.id.etTripDestination)
            val btnSaveTrip = view.findViewById<Button>(R.id.btnSaveTrip)

            closeBtn.setOnClickListener { bottomSheetDialog.dismiss() }

            btnSaveTrip.setOnClickListener {
                val title = etTripTitle.text.toString().trim()
                val destination = etTripDestination.text.toString().trim()

                if (title.isEmpty() || destination.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Trip Created: $title → $destination", Toast.LENGTH_SHORT).show()
                    bottomSheetDialog.dismiss()
                }
            }

            bottomSheetDialog.show()
        }

        bottomSheetDialog.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
