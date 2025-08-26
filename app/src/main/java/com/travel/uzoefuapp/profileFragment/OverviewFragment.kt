package com.travel.uzoefuapp.profileFragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.bookingActivities.BookListActivity
import com.travel.uzoefuapp.databinding.FragmentOverviewBinding
import com.travel.uzoefuapp.fragment.WishlistFragment


class OverviewFragment : Fragment() {
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)

        binding.bookingsCons.setOnClickListener {
            val intent = Intent(requireContext(), BookListActivity::class.java)
            startActivity(intent)
        }

        binding.wishlistLayout.setOnClickListener {
            openFragment(WishlistFragment())
        }

        return binding.root
    }

    private fun openFragment(fragment: Fragment) {
        parentFragmentManager.beginTransaction()
            .replace(R.id.userFrameLayout, fragment)
            .addToBackStack(null)
            .commit()
    }
}