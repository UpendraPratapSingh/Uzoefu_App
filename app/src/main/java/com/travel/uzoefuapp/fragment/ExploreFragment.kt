package com.travel.uzoefuapp.fragment

import CustomProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.travel.uzoefuapp.adapter.DestinationAdapter
import com.travel.uzoefuapp.adapter.DestinationCategoryAdapter
import com.travel.uzoefuapp.databinding.FragmentExploreBinding
import com.travel.uzoefuapp.discoverDestinationModel.DiscoverDestinationResponse
import com.travel.uzoefuapp.discoverDestinationModel.DiscoverDestinationViewModel
import com.travel.uzoefuapp.globalSettings.SettingsActivity
import com.travel.uzoefuapp.notification.NotificationActivity
import com.travel.uzoefuapp.utils.ErrorUtil
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ExploreFragment : Fragment() {
    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!
    private val discoverDestinationViewModel: DiscoverDestinationViewModel by viewModels()
    private val progressDialog by lazy { CustomProgressDialog(requireContext()) }
    private var discoverList: List<DiscoverDestinationResponse.Datum> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        discoverDestinationApi()
        discoverDestinationObserver()

        binding.menuIcon.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.notificationLayout.setOnClickListener {
            val intent = Intent(requireContext(), NotificationActivity::class.java)
            startActivity(intent)
        }

        /*
                val categoriesList = listOf(
                    Category("Pretoria", 64, R.drawable.adventure),
                    Category("Drakensberg", 56, R.drawable.outdoor_adventures),
                    Category("Victoria Falls", 54, R.drawable.food),
                    Category("Kimberly", 46, R.drawable.entertainment),
                    Category("Brits", 30, R.drawable.tours),
                    Category("Polokwane", 18, R.drawable.wellness),
                    Category("Hout Bay", 250, R.drawable.religion),
                    Category("New Castle", 66, R.drawable.religion),
                    Category("Njelele", 131, R.drawable.religion),
                    Category("Wildlife", 65, R.drawable.wildlife),
                    Category("Sabie", 50, R.drawable.tours),
                )
        */

        binding.menuIcon.setOnClickListener {
            val intent = Intent(requireContext(), SettingsActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    private fun discoverDestinationObserver() {
        discoverDestinationViewModel.progressIndicator.observe(viewLifecycleOwner) {

        }
        discoverDestinationViewModel.mCategoryResponse.observe(viewLifecycleOwner) { response ->
            val success = response.peekContent().success
            val message = response.peekContent().message
            discoverList = response.peekContent().data ?: emptyList()

            if (success == true) {
                if (discoverList.isEmpty()) {
                    binding.destinationRecycler.visibility = View.GONE
                    binding.noDataText.visibility = View.VISIBLE
                } else {
                    binding.noDataText.visibility = View.GONE
                    binding.destinationRecycler.visibility = View.VISIBLE
                    binding.destinationRecycler.layoutManager =
                        GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                    val categoryAdapter = DestinationAdapter(requireContext(), discoverList)
                    binding.destinationRecycler.adapter = categoryAdapter

                    binding.categoriesRecycler.layoutManager =
                        GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
                    binding.categoriesRecycler.adapter =
                        DestinationCategoryAdapter(requireContext(), discoverList)

                }
            }
        }
        discoverDestinationViewModel.errorResponse.observe(viewLifecycleOwner) {
            ErrorUtil.handlerGeneralError(requireActivity(), it)
        }
    }

    private fun discoverDestinationApi() {
        discoverDestinationViewModel.discoverDestinationApi(progressDialog, requireActivity())

    }
}