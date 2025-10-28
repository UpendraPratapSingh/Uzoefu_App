package com.travel.uzoefuapp.rewardFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.adapter.Reward
import com.travel.uzoefuapp.adapter.RewardAdapter
import com.travel.uzoefuapp.databinding.FragmentRewardsBinding


class RewardsFragment : Fragment() {
    private var _binding: FragmentRewardsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RewardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRewardsBinding.inflate(inflater, container, false)

        setupRecycler()

        return binding.root
    }

    private fun setupRecycler() {
        val rewardList = listOf(
            Reward("R100 Activity Credit", "Use towards your next activity booking", 400),
            Reward("R50 Meal Credit", "Use towards your next meals", 50),
            Reward("R200 Travel Voucher", "Applicable for your next travel booking", 300),
            Reward("R200 Travel Voucher", "Applicable for your next travel booking", 300),
            Reward("R75 Spa Credit", "Use towards spa services", 300)
        )
        adapter = RewardAdapter(rewardList, requireContext()) {}
        binding.recyclerRewards.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRewards.adapter = adapter
    }
}