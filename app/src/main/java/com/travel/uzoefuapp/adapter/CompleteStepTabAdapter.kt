package com.travel.uzoefuapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class CompleteStepTabAdapter(
    fm: FragmentManager,
    private var fragmentList: MutableList<Fragment>
) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = fragmentList[position]

    override fun getCount(): Int = fragmentList.size

    override fun getItemPosition(`object`: Any): Int {
        // यह force करेगा कि data change होने पर viewpager refresh हो
        return POSITION_NONE
    }

    fun updateData(newFragments: List<Fragment>) {
        fragmentList.clear()
        fragmentList.addAll(newFragments)
        notifyDataSetChanged()
    }
}
