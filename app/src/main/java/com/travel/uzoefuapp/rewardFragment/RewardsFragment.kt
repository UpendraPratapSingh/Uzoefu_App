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
import com.travel.uzoefuapp.adapter.RewardListClickListener
import com.travel.uzoefuapp.databinding.FragmentRewardsBinding
import com.travel.uzoefuapp.redeemRewardModel.RewardRedeemResponse
import com.travel.uzoefuapp.redeemRewardModel.RewardRedeemViewModel
import com.travel.uzoefuapp.rewardModel.RewardViewModel
import com.travel.uzoefuapp.userRedeemReward.UserRedeemRewardBody
import com.travel.uzoefuapp.userRedeemReward.UserRedeemRewardViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RewardsFragment : Fragment(), RewardListClickListener {
    private var _binding: FragmentRewardsBinding? = null
    private val binding get() = _binding!!
    var currentBalance = ""
    private lateinit var adapter: RewardAdapter
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private val rewardViewModel: RewardViewModel by viewModels()
    private val rewardRedeemViewModel: RewardRedeemViewModel by viewModels()
    private var rewardList: List<RewardRedeemResponse.Datum> = ArrayList()
    private val userRedeemRewardViewMode: UserRedeemRewardViewModel by viewModels()

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
        userRedeemRewardObserver()

        return binding.root
    }

    private fun userRedeemRewardObserver() {
        userRedeemRewardViewMode.progressIndicator.observe(viewLifecycleOwner) {

        }
        userRedeemRewardViewMode.userRedeemRewardResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success

            if (success == true) {
                getRewardListApi()
                getRewardObserver()
                getRewardRedeemList()
                getRewardRedeemObserver()
            }
        }
        userRedeemRewardViewMode.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun userRedeemRewardApi(id: Int?, userId: String) {
        val body = UserRedeemRewardBody(
            userId = userId,
            rewardId = id.toString()
        )
        userRedeemRewardViewMode.userRedeemRewardListApi(requireActivity(), progressDialog, body)
    }

    private fun getRewardRedeemObserver() {
        rewardRedeemViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        rewardRedeemViewModel.rewardRedeemResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            rewardList = response.peekContent().data ?: emptyList()

            if (success == true) {
                if (rewardList.isEmpty()) {
                    binding.recyclerRewards.visibility = View.GONE
                    binding.tvNoRewards.visibility = View.VISIBLE
                } else {
                    binding.recyclerRewards.visibility = View.VISIBLE
                    binding.tvNoRewards.visibility = View.GONE
                    adapter = RewardAdapter(rewardList, requireContext(), currentBalance, this) {}
                    binding.recyclerRewards.layoutManager = LinearLayoutManager(requireContext())
                    binding.recyclerRewards.adapter = adapter
                }
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

    override fun onResume() {
        super.onResume()
        getRewardListApi()
        getRewardObserver()
        getRewardRedeemList()
        getRewardRedeemObserver()
    }

    override fun getRewardIdOnClickLis(id: Int?, userId: String) {
        userRedeemRewardApi(id, userId)
    }
}