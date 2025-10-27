package com.travel.uzoefuapp.profileFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.R
import com.travel.uzoefuapp.adapter.Reward
import com.travel.uzoefuapp.adapter.RewardAdapter
import com.travel.uzoefuapp.databinding.FragmentRewardBinding


class RewardFragment : Fragment() {
    private var _binding: FragmentRewardBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RewardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRewardBinding.inflate(inflater, container, false)

        setupRecycler()

        binding.tvRewards.setOnClickListener {
            binding.tvRewards.background = ContextCompat.getDrawable(requireContext(), R.drawable.segment_selected_bg)
            binding.tvRewards.setTextColor(ContextCompat.getColor(requireContext(), R.color.nuetral_bold_color))
            binding.tvHistory.background = null
            binding.tvHistory.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
        }

        binding.tvHistory.setOnClickListener {
            binding.tvHistory.background = ContextCompat.getDrawable(requireContext(), R.drawable.segment_selected_bg)
            binding.tvHistory.setTextColor(ContextCompat.getColor(requireContext(), R.color.nuetral_bold_color))
            binding.tvRewards.background = null
            binding.tvRewards.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
        }
        return binding.root
    }

    private fun setupRecycler() {
        val rewardList = listOf(
            Reward("R100 Activity Credit", "Use towards your next activity booking", 400),
            Reward("R50 Meal Credit", "Use towards your next meals", 50),
            Reward("R200 Travel Voucher", "Applicable for your next travel booking", 300),
            Reward("R75 Spa Credit", "Use towards spa services", 300)
        )

        adapter = RewardAdapter(rewardList, requireContext()) {

        }

        binding.recyclerRewards.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRewards.adapter = adapter
    }
}