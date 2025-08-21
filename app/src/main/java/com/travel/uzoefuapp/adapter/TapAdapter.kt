package com.travel.uzoefuapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.travel.uzoefuapp.profileFragment.CompanyFragment
import com.travel.uzoefuapp.profileFragment.OverviewFragment
import com.travel.uzoefuapp.profileFragment.ProfileDetailFragment
import com.travel.uzoefuapp.profileFragment.RewardFragment

class TabAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    private val fragments = listOf(
        OverviewFragment(),
        ProfileDetailFragment(),
        RewardFragment(),
        CompanyFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
