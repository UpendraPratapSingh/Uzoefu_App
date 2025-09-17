package com.travel.uzoefuapp.profileFragment

import CustomProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.travel.uzoefuapp.adapter.FavouriteAdapter
import com.travel.uzoefuapp.databinding.FragmentProfileDetailBinding
import com.travel.uzoefuapp.getProfileModel.GetProfileViewModel
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileDetailFragment : Fragment() {
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private val getProfileViewModel: GetProfileViewModel by viewModels()


    private var _binding: FragmentProfileDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileDetailBinding.inflate(inflater, container, false)

        getProfileApi()
        getProfileObserver()

        return binding.root
    }

    private fun getProfileObserver() {
        getProfileViewModel.progressIndicator.observe(viewLifecycleOwner){

        }
        getProfileViewModel.mCategoryResponse.observe(viewLifecycleOwner){ response->
            val success = response.peekContent().success
            val message = response.peekContent().message
            val data = response.peekContent().data

            if (success == true){
                binding.firstName.setText(data?.name.toString())
                binding.lastName.setText(data?.lastname.toString())
                binding.email.setText(data?.email.toString())
            }

        }
        getProfileViewModel.errorResponse.observe(viewLifecycleOwner){
            ErrorUtil.handlerGeneralError(requireContext(), it)
        }
    }

    private fun getProfileApi() {
        getProfileViewModel.getProfileApi(progressDialog, requireActivity())

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val distanceRanges = listOf("1 km", "5 km", "10 km", "20 km", "50 km")

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            distanceRanges
        )

        binding.spinnerDistanceRange.setAdapter(adapter)

        binding.categoriesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoriesRecyclerView.adapter = FavouriteAdapter(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
