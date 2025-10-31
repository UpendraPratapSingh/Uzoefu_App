package com.travel.uzoefuapp.rewardFragment

import CustomProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.adapter.RewardRedeemAdapter
import com.travel.uzoefuapp.databinding.FragmentHistoryBinding
import com.travel.uzoefuapp.rewardHistoryModel.RewardHistoryResponse
import com.travel.uzoefuapp.rewardHistoryModel.RewardHistoryViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: RewardRedeemAdapter
    private val rewardHistoryViewModel: RewardHistoryViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private var rewardList: List<RewardHistoryResponse.Datum> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)


        //call api and observer
        getRewardHistoryApi()
        getRewardHistoryObserver()

        return binding.root
    }

    private fun getRewardHistoryObserver() {
        rewardHistoryViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        rewardHistoryViewModel.rewardResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            rewardList = response.peekContent().data ?: emptyList()

            if (success == true) {
                adapter = RewardRedeemAdapter(rewardList)
                binding.recyclerViewRewards.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerViewRewards.adapter = adapter

            }
        }
        rewardHistoryViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getRewardHistoryApi() {
        rewardHistoryViewModel.rewardHistoryListApi(requireActivity(), progressDialog)
    }
}