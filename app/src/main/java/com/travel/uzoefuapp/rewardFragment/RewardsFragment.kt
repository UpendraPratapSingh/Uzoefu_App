package com.travel.uzoefuapp.rewardFragment

import CustomProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.adapter.RewardAdapter
import com.travel.uzoefuapp.databinding.FragmentRewardsBinding
import com.travel.uzoefuapp.redeemRewardModel.RewardRedeemResponse
import com.travel.uzoefuapp.redeemRewardModel.RewardRedeemViewModel
import com.travel.uzoefuapp.rewardModel.RewardViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardsFragment : Fragment() {
    private var _binding: FragmentRewardsBinding? = null
    private val binding get() = _binding!!
    var currentBalance = ""
    private lateinit var adapter: RewardAdapter
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private val rewardViewModel: RewardViewModel by viewModels()
    private val rewardRedeemViewModel: RewardRedeemViewModel by viewModels()
    private var rewardList: List<RewardRedeemResponse.Datum> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRewardsBinding.inflate(inflater, container, false)

        //call api and observer
        getRewardListApi()
        getRewardObserver()
        getRewardRedeemList()
        getRewardRedeemObserver()

        return binding.root
    }

    private fun getRewardRedeemObserver() {
        rewardRedeemViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        rewardRedeemViewModel.rewardRedeemResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            rewardList = response.peekContent().data ?: emptyList()

            if (success == true) {
                adapter = RewardAdapter(rewardList, requireContext(), currentBalance) {}
                binding.recyclerRewards.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerRewards.adapter = adapter
            }

        }
        rewardRedeemViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }

    }

    private fun getRewardRedeemList() {
        rewardRedeemViewModel.rewardRedeemListApi(requireActivity(), progressDialog)
    }

    private fun getRewardObserver() {
        rewardViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        rewardViewModel.rewardResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val data = response.peekContent().rewardPoints
            currentBalance = data.toString()

            if (success == true) {
                binding.tvAvailablePoints.text = data
            }
        }
        rewardViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getRewardListApi() {
        rewardViewModel.rewardListApi(requireActivity(), progressDialog)
    }
}