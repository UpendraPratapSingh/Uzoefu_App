package com.travel.uzoefuapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.travel.uzoefuapp.businessFragment.BusinessActivityFragment
import com.travel.uzoefuapp.businessFragment.BusinessBookingFragment
import com.travel.uzoefuapp.businessFragment.BusinessOverviewFragment
import com.travel.uzoefuapp.businessFragment.BusinessProfileFragment

class BusinessTabAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    private val fragments = listOf(
        BusinessOverviewFragment(),
        BusinessProfileFragment(),
        BusinessActivityFragment(),
        BusinessBookingFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
