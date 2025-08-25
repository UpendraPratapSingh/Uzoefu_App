package com.travel.uzoefuapp.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.travel.uzoefuapp.bookingfragment.ActiveFragment
import com.travel.uzoefuapp.bookingfragment.CancelledFragment
import com.travel.uzoefuapp.bookingfragment.PastFragment


class BookingTabAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ActiveFragment()
            1 -> PastFragment()
            else -> CancelledFragment()
        }
    }
}
