package com.travel.uzoefuapp.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.tabs.TabLayoutMediator
import com.travel.uzoefuapp.adapter.TabAdapter
import com.travel.uzoefuapp.databinding.FragmentProfileBinding
import com.travel.uzoefuapp.globalSettings.SettingsActivity
import com.travel.uzoefuapp.notification.NotificationActivity


class ProfileFragment(private var i: Int) : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        binding.menuIcon.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

     /*   val adapter = TabAdapter(requireActivity())
        binding.viewPager.adapter = adapter
*/
        val adapter = TabAdapter(requireActivity())
        binding.viewPager.adapter = adapter

// Set default tab based on i
        binding.viewPager.currentItem = when (i) {
            2 -> 2   // Reviews
            1 -> 1   // Profile
            else -> 0 // Overview
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Overview"
                1 -> "Profile"
                2 -> "Reviews"
                3 -> "Rewards"
                else -> ""
            }
        }.attach()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
