package com.travel.uzoefuapp.profileFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.databinding.FragmentRewardBinding
import com.travel.uzoefuapp.rewardFragment.HistoryFragment
import com.travel.uzoefuapp.rewardFragment.RewardsFragment


class RewardFragment : Fragment() {
    private var _binding: FragmentRewardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRewardBinding.inflate(inflater, container, false)

        replaceChildFragment(RewardsFragment())


        binding.tvRewards.setOnClickListener {
            selectTab(isRewards = true)
            replaceChildFragment(RewardsFragment())
        }

        binding.tvHistory.setOnClickListener {
            selectTab(isRewards = false)
            replaceChildFragment(HistoryFragment())
        }

        return binding.root
    }

    private fun selectTab(isRewards: Boolean) {
        if (isRewards) {
            binding.tvRewards.setBackgroundResource(R.drawable.segment_selected_bg)
            binding.tvHistory.setBackgroundResource(android.R.color.transparent)
            binding.tvRewards.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.nuetral_bold_color
                )
            )
            binding.tvHistory.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
        } else {
            binding.tvHistory.setBackgroundResource(R.drawable.segment_selected_bg)
            binding.tvRewards.setBackgroundResource(android.R.color.transparent)
            binding.tvHistory.setTextColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.nuetral_bold_color
                )
            )
            binding.tvRewards.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
        }
    }

    private fun replaceChildFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.userFrameLayout, fragment)
            .commit()
    }
}