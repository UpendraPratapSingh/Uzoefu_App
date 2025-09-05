package com.travel.uzoefuapp.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.travel.uzoefuapp.businessBookingFragment.BusinessActiveFragment
import com.travel.uzoefuapp.businessBookingFragment.BusinessCancelledFragment
import com.travel.uzoefuapp.businessBookingFragment.BusinessPastFragment

class BusinessBookingTabAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BusinessActiveFragment()
            1 -> BusinessPastFragment()
            else -> BusinessCancelledFragment()
        }
    }
}