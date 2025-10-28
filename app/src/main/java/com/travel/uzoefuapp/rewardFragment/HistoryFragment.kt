package com.travel.uzoefuapp.rewardFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.adapter.RewardItem
import com.travel.uzoefuapp.adapter.RewardRedeemAdapter
import com.travel.uzoefuapp.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RewardRedeemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)

        val rewardList = listOf(
            RewardItem("R100 Activity Credit", "-500 pts", "12 October 2027", "UZR1234-300978-25"),
            RewardItem("R75 Meal Voucher", "-500 pts", "12 October 2027", "UZR1234-300978-25")
        )

        adapter = RewardRedeemAdapter(rewardList)
        binding.recyclerViewRewards.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewRewards.adapter = adapter

        return binding.root
    }
}